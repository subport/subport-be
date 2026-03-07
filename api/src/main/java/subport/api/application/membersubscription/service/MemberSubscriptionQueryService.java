package subport.api.application.membersubscription.service;

import static java.time.temporal.ChronoUnit.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionsResponse;
import subport.api.application.membersubscription.port.in.dto.MemberSubscriptionSummary;
import subport.api.application.membersubscription.port.in.dto.SpendingRecordSummary;
import subport.api.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.api.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.common.exception.CustomException;
import subport.domain.membersubscription.MemberSubscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSubscriptionQueryService implements MemberSubscriptionQueryUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadSpendingRecordPort loadSpendingRecordPort;

	@Override
	public GetMemberSubscriptionResponse getMemberSubscription(
		Long memberId,
		Long memberSubscriptionId,
		LocalDate currentDate
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_ACCESS_FORBIDDEN);
		}

		List<SpendingRecordSummary> spendingRecords = loadSpendingRecordPort.loadSpendingRecords(memberSubscriptionId)
			.stream()
			.map(SpendingRecordSummary::from)
			.toList();

		if (memberSubscription.isActive()) {
			LocalDate nextPaymentDate = memberSubscription.getNextPaymentDate();
			LocalDate lastPaymentDate = memberSubscription.getLastPaymentDate();

			long elapsedDays = DAYS.between(lastPaymentDate, currentDate);
			long totalDays = DAYS.between(lastPaymentDate, nextPaymentDate);
			int paymentProgressPercent = (int)((double)elapsedDays / totalDays * 100);

			return GetMemberSubscriptionResponse.forActive(
				memberSubscription,
				currentDate,
				paymentProgressPercent,
				spendingRecords
			);
		}

		return GetMemberSubscriptionResponse.forInActive(
			memberSubscription,
			spendingRecords
		);
	}

	@Override
	public GetMemberSubscriptionsResponse getMemberSubscriptions(
		Long memberId,
		boolean active,
		String sortBy,
		LocalDate currentDate
	) {
		List<MemberSubscription> memberSubscriptions = loadMemberSubscriptionPort.loadMemberSubscriptions(
			memberId,
			active,
			sortBy
		);

		if (!active) {
			return new GetMemberSubscriptionsResponse(
				null,
				memberSubscriptions.stream()
					.map(MemberSubscriptionSummary::from)
					.toList()
			);
		}

		List<ComputedMemberSubscription> computed = memberSubscriptions.stream()
			.map(ms -> {
				BigDecimal actualPaymentAmount = ms.calculateActualPaymentAmount();

				LocalDate nextPaymentDate = ms.getNextPaymentDate();
				long daysUntilPayment = DAYS.between(currentDate, nextPaymentDate);
				if (currentDate.isEqual(ms.getLastPaymentDate()) || currentDate.isEqual(nextPaymentDate)) {
					daysUntilPayment = 0;
				}

				return new ComputedMemberSubscription(
					ms,
					actualPaymentAmount,
					daysUntilPayment
				);
			})
			.toList();

		BigDecimal currentMonthTotalAmount = computed.stream()
			.filter(c ->
				YearMonth.from(c.memberSubscription.getLastPaymentDate())
					.equals(YearMonth.from(currentDate))
			)
			.map(ComputedMemberSubscription::actualPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);

		if (sortBy.equals("type")) {
			return new GetMemberSubscriptionsResponse(
				currentMonthTotalAmount,
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
			currentMonthTotalAmount,
			computed.stream()
				.map(c -> MemberSubscriptionSummary.of(
					c.memberSubscription,
					c.actualPaymentAmount,
					c.daysUntilPayment
				))
				.toList()
		);
	}

	private record ComputedMemberSubscription(
		MemberSubscription memberSubscription,
		BigDecimal actualPaymentAmount,
		long daysUntilPayment
	) {
	}
}
