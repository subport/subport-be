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
	Long daysUntilPayment,
	Long daysSinceStart,
	Integer paymentProgressPercent,
	LocalDate paymentDate,
	Integer reminderDaysBefore,
	boolean active,
	Boolean dutchPay,
	BigDecimal actualPayment,
	List<SpendingRecordSummary> spendingRecords,
	String memo
) {

	public static GetMemberSubscriptionResponse forActive(
		MemberSubscription memberSubscription,
		LocalDate now,
		int paymentProgressPercent,
		List<SpendingRecordSummary> spendingRecords
	) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();
		BigDecimal actualPaymentAmount = memberSubscription.calculateActualPaymentAmount();

		return new GetMemberSubscriptionResponse(
			memberSubscription.getId(),
			subscription.getId(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			plan.getId(),
			plan.getName(),
			plan.getAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getAmountUnit().name(),
			plan.getDurationMonths(),
			ChronoUnit.DAYS.between(now, memberSubscription.getNextPaymentDate()),
			ChronoUnit.DAYS.between(memberSubscription.getStartDate(), now),
			paymentProgressPercent,
			memberSubscription.getNextPaymentDate(),
			memberSubscription.getReminderDaysBefore(),
			memberSubscription.isActive(),
			memberSubscription.isDutchPay(),
			actualPaymentAmount.setScale(0, RoundingMode.HALF_UP),
			spendingRecords,
			memberSubscription.getMemo()
		);
	}

	public static GetMemberSubscriptionResponse forInActive(
		MemberSubscription memberSubscription,
		List<SpendingRecordSummary> spendingRecords
	) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();
		BigDecimal actualPaymentAmount = memberSubscription.calculateActualPaymentAmount();

		return new GetMemberSubscriptionResponse(
			memberSubscription.getId(),
			subscription.getId(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			plan.getId(),
			plan.getName(),
			plan.getAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getAmountUnit().name(),
			plan.getDurationMonths(),
			null,
			null,
			null,
			memberSubscription.getLastPaymentDate(),
			null,
			memberSubscription.isActive(),
			null,
			actualPaymentAmount.setScale(0, RoundingMode.HALF_UP),
			spendingRecords,
			memberSubscription.getMemo()
		);
	}
}
