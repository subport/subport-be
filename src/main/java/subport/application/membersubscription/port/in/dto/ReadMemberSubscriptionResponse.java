package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;

public record ReadMemberSubscriptionResponse(
	Long id,
	Long subscriptionId,
	String name,
	String logoImageUrl,
	Long planId,
	String planName,
	BigDecimal planAmount,
	String planAmountUnit,
	int durationMonths,
	long daysUntilPayment,
	long monthsSinceStart,
	int paymentProgressPercent,
	LocalDate nextPaymentDate,
	Integer reminderDaysBefore,
	boolean active,
	boolean dutchPay,
	BigDecimal actualPayment,
	List<SpendingRecordSummary> spendingRecords,
	String memo
) {

	public static ReadMemberSubscriptionResponse from(
		MemberSubscriptionDetail detail,
		LocalDate now,
		int paymentProgressPercent,
		BigDecimal actualPayment,
		List<SpendingRecordSummary> spendingRecords
	) {
		return new ReadMemberSubscriptionResponse(
			detail.id(),
			detail.subscriptionId(),
			detail.subscriptionName(),
			detail.subscriptionLogoImageUrl(),
			detail.planId(),
			detail.planName(),
			detail.planAmount(),
			detail.planAmountUnit().name(),
			detail.durationMonths(),
			ChronoUnit.DAYS.between(now, detail.nextPaymentDate()),
			ChronoUnit.MONTHS.between(detail.startDate(), now),
			paymentProgressPercent,
			detail.nextPaymentDate(),
			detail.reminderDaysBefore(),
			detail.active(),
			detail.dutchPay(),
			actualPayment.setScale(0, RoundingMode.HALF_UP),
			spendingRecords,
			detail.memo()
		);
	}
}
