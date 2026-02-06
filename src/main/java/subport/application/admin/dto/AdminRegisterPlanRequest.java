package subport.application.admin.dto;

import java.math.BigDecimal;

public record AdminRegisterPlanRequest(
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {
}
