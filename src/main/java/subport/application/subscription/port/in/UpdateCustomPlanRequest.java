package subport.application.subscription.port.in;

import java.math.BigDecimal;

public record UpdateCustomPlanRequest(
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
