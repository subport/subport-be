package subport.application.subscription.port.out;

import java.math.BigDecimal;
import java.math.RoundingMode;

import subport.domain.subscription.Plan;

public record ReadPlanResponse(
	Long id,
	String name,
	BigDecimal amount,
	String amountUnit,
	int durationMonths
) {

	public static ReadPlanResponse fromDomain(Plan plan) {
		return new ReadPlanResponse(
			plan.getId(),
			plan.getName(),
			plan.getAmount().setScale(0, RoundingMode.HALF_UP),
			plan.getAmountUnit().name(),
			plan.getDurationMonths()
		);
	}
}
