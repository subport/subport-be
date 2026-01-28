package subport.application.spendingrecord.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.spendingrecord.port.in.GetMonthlyCalendarUseCase;
import subport.application.spendingrecord.port.in.dto.GetMonthlyCalendarResponse;
import subport.application.spendingrecord.port.in.dto.PaymentDateInfo;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMonthlyCalendarService implements GetMonthlyCalendarUseCase {

	private final LoadSpendingRecordPort loadSpendingRecordPort;
	private final LoadMemberSubscriptionPort memberSubscriptionPort;

	@Override
	public GetMonthlyCalendarResponse get(Long memberId, YearMonth targetYearMonth) {
		YearMonth previousYearMonth = targetYearMonth.minusMonths(1);

		LocalDate start = previousYearMonth.atDay(1);
		LocalDate end = targetYearMonth.plusMonths(1).atDay(1);

		Map<YearMonth, List<SpendingRecord>> spendingRecords = loadSpendingRecordPort
			.loadSpendingRecords(memberId, start, end).stream()
			.collect(Collectors.groupingBy(sr -> YearMonth.from(sr.getPaymentDate())));

		Map<YearMonth, List<MemberSubscription>> memberSubscriptions = memberSubscriptionPort
			.loadMemberSubscriptions(memberId, start, end).stream()
			.collect(Collectors.groupingBy(ms -> YearMonth.from(ms.getLastPaymentDate())));

		List<SpendingRecord> targetSpendingRecords = spendingRecords.getOrDefault(targetYearMonth, List.of());
		List<MemberSubscription> targetMemberSubscriptions = memberSubscriptions.getOrDefault(
			targetYearMonth, List.of());

		Map<Integer, PaymentDateInfo> paymentDates = buildPaymentDatesMap(
			targetSpendingRecords,
			targetMemberSubscriptions,
			targetYearMonth
		);

		BigDecimal totalAmount = calculateTotalAmount(targetSpendingRecords, targetMemberSubscriptions);
		BigDecimal previousMonthTotalAmount = calculateTotalAmount(
			spendingRecords.getOrDefault(previousYearMonth, List.of()),
			memberSubscriptions.getOrDefault(previousYearMonth, List.of())
		);

		return new GetMonthlyCalendarResponse(
			totalAmount,
			totalAmount.subtract(previousMonthTotalAmount),
			paymentDates
		);
	}

	private Map<Integer, PaymentDateInfo> buildPaymentDatesMap(
		List<SpendingRecord> spendingRecords,
		List<MemberSubscription> memberSubscriptions,
		YearMonth yearMonth
	) {
		Map<Integer, PaymentDateInfo> paymentDates = new LinkedHashMap<>();
		for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
			int day = i;

			boolean hasSpending = spendingRecords.stream()
				.anyMatch(sr -> sr.getPaymentDate().getDayOfMonth() == day);

			boolean hasSubscription = memberSubscriptions.stream()
				.anyMatch(ms -> ms.getLastPaymentDate().getDayOfMonth() == day);

			if (hasSpending || hasSubscription) {
				paymentDates.put(
					day,
					new PaymentDateInfo(hasSpending, hasSubscription)
				);
			}
		}

		return paymentDates;
	}

	private BigDecimal calculateTotalAmount(
		List<SpendingRecord> spendingRecords,
		List<MemberSubscription> memberSubscriptions
	) {
		BigDecimal totalSpendingAmount = calculateTotalSpendingAmount(spendingRecords);
		BigDecimal totalActualPaymentAmount = calculateTotalActualPaymentAmount(memberSubscriptions);

		return totalSpendingAmount.add(totalActualPaymentAmount);
	}

	private BigDecimal calculateTotalSpendingAmount(List<SpendingRecord> spendingRecords) {
		return spendingRecords.stream()
			.map(SpendingRecord::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);
	}

	private BigDecimal calculateTotalActualPaymentAmount(List<MemberSubscription> memberSubscriptions) {
		return memberSubscriptions.stream()
			.map(MemberSubscription::calculateActualPaymentAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add)
			.setScale(0, RoundingMode.HALF_UP);
	}
}
