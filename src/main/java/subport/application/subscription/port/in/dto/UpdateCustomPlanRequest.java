package subport.application.subscription.port.in.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCustomPlanRequest(
	@NotBlank
	@Size(max = 10)
	String name,

	@NotNull
	BigDecimal amount,

	@NotBlank
	String amountUnit,

	@Min(1)
	int durationMonths
) {
}
