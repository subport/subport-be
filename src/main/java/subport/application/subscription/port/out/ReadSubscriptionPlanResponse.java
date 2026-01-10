package subport.application.subscription.port.out;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.domain.subscription.SubscriptionPlan;

public record ReadSubscriptionPlanResponse(
	Long id,
	String planName,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {

	public static ReadSubscriptionPlanResponse fromDomain(SubscriptionPlan subscriptionPlan) {
		return new ReadSubscriptionPlanResponse(
			subscriptionPlan.getId(),
			subscriptionPlan.getPlanName(),
			subscriptionPlan.getAmount().setScale(0, RoundingMode.HALF_UP),
			subscriptionPlan.getAmountUnit().name(),
			subscriptionPlan.getDurationMonths()
		);
	}
}
