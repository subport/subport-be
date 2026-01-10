package subport.application.subscription.port.in;

import java.math.BigDecimal;

public record RegisterCustomSubscriptionPlanRequest(
	String planName,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
