package subport.application.membersubscription.service;

import static java.time.temporal.ChronoUnit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
import subport.domain.subscription.AmountUnit;

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
		LocalDate lastPaymentDate = memberSubscriptionDetail.lastPaymentDate();

		LocalDate now = LocalDate.now();
		long elapsedDays = DAYS.between(lastPaymentDate, now);
		long totalDays = DAYS.between(lastPaymentDate, nextPaymentDate);
		int paymentProgressPercent = (int)((double)elapsedDays / totalDays * 100);

		BigDecimal actualPaymentAmount = calculateActualPaymentAmount(memberSubscriptionDetail);

		List<SpendingRecordSummary> spendingRecords = loadSpendingRecordPort.loadSpendingRecords(memberSubscriptionId)
			.stream()
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

		List<ComputedMemberSubscription> computed = memberSubscriptionDetails.stream()
			.map(ms -> {
				BigDecimal actualPaymentAmount = calculateActualPaymentAmount(ms);
				return new ComputedMemberSubscription(
					ms,
					actualPaymentAmount,
					DAYS.between(LocalDate.now(), ms.nextPaymentDate())
				);
			})
			.toList();

		BigDecimal totalAmount = computed.stream()
			.map(ComputedMemberSubscription::actualPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);

		if (active & sortBy.equals("type")) {
			return new ListMemberSubscriptionsResponse(
				totalAmount,
				computed.stream()
					.collect(Collectors.groupingBy(
						c -> c.detail.subscriptionType().getDisplayName(),
						TreeMap::new,
						Collectors.mapping(c -> MemberSubscriptionSummary.from(
								c.detail,
								c.actualPaymentAmount,
								c.daysUntilPayment
							),
							Collectors.toList()
						)
					))
			);
		}

		return new ListMemberSubscriptionsResponse(
			totalAmount,
			computed.stream()
				.map(c -> MemberSubscriptionSummary.from(
					c.detail,
					c.actualPaymentAmount,
					c.daysUntilPayment
				))
				.toList()
		);
	}

	private BigDecimal calculateActualPaymentAmount(MemberSubscriptionDetail detail) {
		boolean dutchPay = detail.dutchPay();
		AmountUnit amountUnit = detail.planAmountUnit();
		BigDecimal planAmount = detail.planAmount();

		if (dutchPay) {
			return detail.dutchPayAmount();
		}
		if (amountUnit.equals(AmountUnit.USD)) {
			return planAmount.multiply(detail.exchangeRate())
				.setScale(0, RoundingMode.HALF_UP);
		}

		return planAmount;
	}

	private record ComputedMemberSubscription(
		MemberSubscriptionDetail detail,
		BigDecimal actualPaymentAmount,
		long daysUntilPayment
	) {
	}
}
