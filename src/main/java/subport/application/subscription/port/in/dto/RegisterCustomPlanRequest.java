package subport.application.subscription.port.in.dto;

import java.math.BigDecimal;

public record RegisterCustomPlanRequest(
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
