package subport.admin.application.plan.dto;

import java.math.BigDecimal;

public record RegisterPlanRequest(
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
