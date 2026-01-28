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
import subport.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionsRequest;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionsResponse;
import subport.application.membersubscription.port.in.dto.MemberSubscriptionSummary;
import subport.application.membersubscription.port.in.dto.SpendingRecordSummary;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSubscriptionQueryService implements MemberSubscriptionQueryUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadSpendingRecordPort loadSpendingRecordPort;

	@Override
	public GetMemberSubscriptionResponse getMemberSubscription(Long memberId, Long memberSubscriptionId) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		LocalDate nextPaymentDate = memberSubscription.getNextPaymentDate();
		LocalDate lastPaymentDate = memberSubscription.getLastPaymentDate();

		LocalDate now = LocalDate.now();
		long elapsedDays = DAYS.between(lastPaymentDate, now);
		long totalDays = DAYS.between(lastPaymentDate, nextPaymentDate);
		int paymentProgressPercent = (int)((double)elapsedDays / totalDays * 100);

		BigDecimal actualPaymentAmount = calculateActualPaymentAmount(memberSubscription);

		List<SpendingRecordSummary> spendingRecords = loadSpendingRecordPort.loadSpendingRecords(memberSubscriptionId)
			.stream()
			.map(SpendingRecordSummary::fromDomain)
			.toList();

		return GetMemberSubscriptionResponse.of(
			memberSubscription,
			now,
			paymentProgressPercent,
			actualPaymentAmount,
			spendingRecords
		);
	}

	@Override
	public GetMemberSubscriptionsResponse getMemberSubscriptions(Long memberId, GetMemberSubscriptionsRequest request) {
		boolean active = request.active();
		String sortBy = request.sortBy();

		List<MemberSubscription> memberSubscriptions = loadMemberSubscriptionPort.loadMemberSubscriptions(
			memberId,
			active,
			sortBy
		);

		List<ComputedMemberSubscription> computed = memberSubscriptions.stream()
			.map(ms -> {
				BigDecimal actualPaymentAmount = calculateActualPaymentAmount(ms);
				return new ComputedMemberSubscription(
					ms,
					actualPaymentAmount,
					DAYS.between(LocalDate.now(), ms.getNextPaymentDate())
				);
			})
			.toList();

		BigDecimal totalAmount = computed.stream()
			.map(ComputedMemberSubscription::actualPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);

		if (active & sortBy.equals("type")) {
			return new GetMemberSubscriptionsResponse(
				totalAmount,
				computed.stream()
					.collect(Collectors.groupingBy(
						c -> c.memberSubscription.getSubscription().getType().getDisplayName(),
						TreeMap::new,
						Collectors.mapping(c -> MemberSubscriptionSummary.of(
								c.memberSubscription,
								c.actualPaymentAmount,
								c.daysUntilPayment
							),
							Collectors.toList()
						)
					))
			);
		}

		return new GetMemberSubscriptionsResponse(
			totalAmount,
			computed.stream()
				.map(c -> MemberSubscriptionSummary.of(
					c.memberSubscription,
					c.actualPaymentAmount,
					c.daysUntilPayment
				))
				.toList()
		);
	}

	private BigDecimal calculateActualPaymentAmount(MemberSubscription memberSubscription) {
		boolean dutchPay = memberSubscription.isDutchPay();
		Plan plan = memberSubscription.getPlan();
		AmountUnit amountUnit = plan.getAmountUnit();
		BigDecimal planAmount = plan.getAmount();

		if (dutchPay) {
			return memberSubscription.getDutchPayAmount();
		}
		if (amountUnit.equals(AmountUnit.USD)) {
			return planAmount.multiply(memberSubscription.getExchangeRate())
				.setScale(0, RoundingMode.HALF_UP);
		}

		return planAmount;
	}

	private record ComputedMemberSubscription(
		MemberSubscription memberSubscription,
		BigDecimal actualPaymentAmount,
		long daysUntilPayment
	) {
	}
}
