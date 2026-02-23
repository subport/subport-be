package subport.admin.application.plan.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import subport.domain.plan.Plan;

public record PlanResponse(
	Long id,
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths,
	LocalDate createdAt,
	LocalDate lastModifiedAt
) {

	public static PlanResponse from(Plan plan) {
		return new PlanResponse(
			plan.getId(),
			plan.getName(),
			plan.getAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getAmountUnit().name(),
			plan.getDurationMonths(),
			plan.getCreatedAt().toLocalDate(),
			plan.getLastModifiedAt().toLocalDate()
		);
	}
}
