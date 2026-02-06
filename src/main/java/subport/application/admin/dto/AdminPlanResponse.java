package subport.application.admin.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import subport.domain.subscription.Plan;

public record AdminPlanResponse(
	Long id,
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths,
	LocalDate createdAt,
	LocalDate lastModifiedAt
) {

	public static AdminPlanResponse from(Plan plan) {
		return new AdminPlanResponse(
			plan.getId(),
			plan.getName(),
			plan.getAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getAmountUnit().getSymbol(),
			plan.getDurationMonths(),
			plan.getCreatedAt().toLocalDate(),
			plan.getLastModifiedAt().toLocalDate()
		);
	}
}
