package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

public record GetMemberSubscriptionResponse(
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

	public static GetMemberSubscriptionResponse of(
		MemberSubscription memberSubscription,
		LocalDate now,
		int paymentProgressPercent,
		BigDecimal actualPayment,
		List<SpendingRecordSummary> spendingRecords
	) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();

		return new GetMemberSubscriptionResponse(
			memberSubscription.getId(),
			subscription.getId(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			plan.getId(),
			plan.getName(),
			plan.getAmount(),
			plan.getAmountUnit().name(),
			plan.getDurationMonths(),
			ChronoUnit.DAYS.between(now, memberSubscription.getNextPaymentDate()),
			ChronoUnit.MONTHS.between(memberSubscription.getStartDate(), now),
			paymentProgressPercent,
			memberSubscription.getNextPaymentDate(),
			memberSubscription.getReminderDaysBefore(),
			memberSubscription.isActive(),
			memberSubscription.isDutchPay(),
			actualPayment.setScale(0, RoundingMode.HALF_UP),
			spendingRecords,
			memberSubscription.getMemo()
		);
	}
}
