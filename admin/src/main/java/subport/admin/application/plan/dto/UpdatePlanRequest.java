package subport.admin.application.plan.dto;

import java.math.BigDecimal;

public record UpdatePlanRequest(
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
