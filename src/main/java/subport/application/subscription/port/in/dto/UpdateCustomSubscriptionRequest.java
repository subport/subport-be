package subport.application.subscription.port.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCustomSubscriptionRequest(
	@NotBlank
	@Size(max = 10)
	String name,

	@NotBlank
	String type
) {
}
