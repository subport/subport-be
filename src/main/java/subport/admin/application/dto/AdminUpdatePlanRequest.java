package subport.admin.application.dto;

import java.math.BigDecimal;

public record AdminUpdatePlanRequest(
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
