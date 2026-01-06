package subport.application.subscription.port.in;

public record RegisterCustomSubscriptionPlanRequest(
	String planName,
	int amount,
	String amountUnit,
	int durationMonths
) {
}
