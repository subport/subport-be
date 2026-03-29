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
import subport.api.application.membersubscription.port.in.dto.GetMonthlyExpenseSummaryResponse;
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

		if (sortBy.equals("type")) {
			return new GetMemberSubscriptionsResponse(
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
			computed.stream()
				.map(c -> MemberSubscriptionSummary.of(
					c.memberSubscription,
					c.actualPaymentAmount,
					c.daysUntilPayment
				))
				.toList()
		);
	}

	@Override
	public GetMonthlyExpenseSummaryResponse getMonthlyExpenseSummary(Long memberId, LocalDate currentDate) {
		LocalDate start = currentDate.withDayOfMonth(1);
		LocalDate end = start.plusMonths(1);

		List<MemberSubscription> memberSubscriptions = loadMemberSubscriptionPort.loadMemberSubscriptionsForMonth(
			memberId,
			start,
			end
		);

		BigDecimal currentMonthPaidAmount = calculateCurrentMonthPaidAmount(currentDate, memberSubscriptions);
		BigDecimal currentMonthScheduledAmount = calculateCurrentMonthScheduledAmount(currentDate, memberSubscriptions);
		BigDecimal currentMonthTotalAmount = currentMonthPaidAmount.add(currentMonthScheduledAmount);

		int paymentProgressPercent = calculatePaymentProgressPercent(currentMonthPaidAmount, currentMonthTotalAmount);

		return new GetMonthlyExpenseSummaryResponse(
			currentMonthPaidAmount,
			currentMonthTotalAmount,
			paymentProgressPercent
		);
	}

	private BigDecimal calculateCurrentMonthPaidAmount(
		LocalDate currentDate,
		List<MemberSubscription> memberSubscriptions
	) {
		return memberSubscriptions.stream()
			.filter(ms ->
				!ms.getLastPaymentDate().isAfter(currentDate) &&
					YearMonth.from(ms.getLastPaymentDate())
						.equals(YearMonth.from(currentDate))
			)
			.map(MemberSubscription::calculateActualPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);
	}

	private BigDecimal calculateCurrentMonthScheduledAmount(
		LocalDate currentDate,
		List<MemberSubscription> memberSubscriptions
	) {
		return memberSubscriptions.stream()
			.filter(ms ->
				ms.getNextPaymentDate().isAfter(currentDate) &&
					YearMonth.from(ms.getLastPaymentDate())
						.equals(YearMonth.from(currentDate))
			)
			.map(MemberSubscription::calculateActualPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);
	}

	private int calculatePaymentProgressPercent(
		BigDecimal currentMonthPaidAmount,
		BigDecimal currentMonthTotalAmount
	) {
		if (currentMonthPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
			return currentMonthPaidAmount
				.multiply(BigDecimal.valueOf(100))
				.divide(currentMonthTotalAmount, 0, RoundingMode.HALF_UP)
				.intValue();
		}
		return 0;
	}

	private record ComputedMemberSubscription(
		MemberSubscription memberSubscription,
		BigDecimal actualPaymentAmount,
		long daysUntilPayment
	) {
	}
}
