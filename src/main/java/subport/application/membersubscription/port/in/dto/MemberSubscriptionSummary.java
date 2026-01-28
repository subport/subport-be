package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

public record MemberSubscriptionSummary(
	Long id,
	String name,
	String logoImageUrl,
	BigDecimal amount,
	int period,
	long daysUntilPayment
) {

	public static MemberSubscriptionSummary of(
		MemberSubscription memberSubscription,
		BigDecimal actualPaymentAmount,
		long daysUntilPayment
	) {
		Subscription subscription = memberSubscription.getSubscription();
		Plan plan = memberSubscription.getPlan();

		return new MemberSubscriptionSummary(
			memberSubscription.getId(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			actualPaymentAmount.setScale(0, RoundingMode.HALF_UP),
			plan.getDurationMonths(),
			daysUntilPayment
		);
	}
}
