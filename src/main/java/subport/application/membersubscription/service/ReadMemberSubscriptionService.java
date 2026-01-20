package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.ReadMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.dto.ListMemberSubscriptionsRequest;
import subport.application.membersubscription.port.in.dto.ListMemberSubscriptionsResponse;
import subport.application.membersubscription.port.in.dto.MemberSubscriptionSummary;
import subport.application.membersubscription.port.in.dto.ReadMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.SpendingRecordSummary;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.domain.subscription.SubscriptionAmountUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadMemberSubscriptionService implements ReadMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadSpendingRecordPort loadSpendingRecordPort;

	@Override
	public ReadMemberSubscriptionResponse read(Long memberId, Long memberSubscriptionId) {
		MemberSubscriptionDetail memberSubscriptionDetail = loadMemberSubscriptionPort.loadDetail(memberSubscriptionId);

		if (!memberSubscriptionDetail.memberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		LocalDate nextPaymentDate = memberSubscriptionDetail.nextPaymentDate();
		LocalDate currentPaymentDate = nextPaymentDate.minusMonths(memberSubscriptionDetail.durationMonths());

		LocalDate now = LocalDate.now();
		long elapsedDays = ChronoUnit.DAYS.between(currentPaymentDate, now);
		long totalDays = ChronoUnit.DAYS.between(currentPaymentDate, nextPaymentDate);
		int paymentProgressPercent = (int)((double)elapsedDays / totalDays * 100);

		BigDecimal actualPaymentAmount = calculateActualPaymentAmount(memberSubscriptionDetail);

		List<SpendingRecordSummary> spendingRecords = loadSpendingRecordPort.loadRecent3ByMemberIdAndSubscriptionName(
				memberId,
				memberSubscriptionDetail.subscriptionName()
			).stream()
			.map(SpendingRecordSummary::fromDomain)
			.toList();

		return ReadMemberSubscriptionResponse.from(
			memberSubscriptionDetail,
			now,
			paymentProgressPercent,
			actualPaymentAmount,
			spendingRecords
		);
	}

	@Override
	public ListMemberSubscriptionsResponse list(Long memberId, ListMemberSubscriptionsRequest request) {
		boolean active = request.active();
		String sortBy = request.sortBy();

		List<MemberSubscriptionDetail> memberSubscriptionDetails = loadMemberSubscriptionPort.loadDetails(
			memberId,
			active,
			sortBy
		);

		LocalDate now = LocalDate.now();

		if (active & sortBy.equals("type")) {
			return new ListMemberSubscriptionsResponse(
				memberSubscriptionDetails.stream()
					.collect(Collectors.groupingBy(
						ms -> ms.subscriptionType().getDisplayName(),
						TreeMap::new,
						Collectors.mapping(ms -> MemberSubscriptionSummary.from(
								ms,
								calculateActualPaymentAmount(ms),
								ChronoUnit.DAYS.between(now, ms.nextPaymentDate())
							),
							Collectors.toList()
						)
					))
			);
		}

		return new ListMemberSubscriptionsResponse(
			memberSubscriptionDetails.stream()
				.map(ms -> MemberSubscriptionSummary.from(
					ms,
					calculateActualPaymentAmount(ms),
					ChronoUnit.DAYS.between(now, ms.nextPaymentDate())
				))
				.toList()
		);
	}

	private BigDecimal calculateActualPaymentAmount(MemberSubscriptionDetail detail) {
		boolean dutchPay = detail.dutchPay();
		SubscriptionAmountUnit amountUnit = detail.planAmountUnit();
		BigDecimal planAmount = detail.planAmount();

		if (dutchPay) {
			return detail.dutchPayAmount();
		}
		if (amountUnit.equals(SubscriptionAmountUnit.USD)) {
			return planAmount.multiply(detail.exchangeRate())
				.setScale(0, RoundingMode.HALF_UP);
		}

		return planAmount;
	}
}
