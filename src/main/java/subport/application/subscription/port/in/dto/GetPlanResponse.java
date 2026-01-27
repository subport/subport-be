package subport.application.subscription.port.in.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.domain.subscription.Plan;

public record GetPlanResponse(
	Long id,
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths,
	boolean defaultProvided
) {

	public static GetPlanResponse from(Plan plan) {
		return new GetPlanResponse(
			plan.getId(),
			plan.getName(),
			plan.getAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getAmountUnit().name(),
			plan.getDurationMonths(),
			plan.isSystemProvided()
		);
	}
}
