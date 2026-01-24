package subport.application.membersubscription.port.in.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateMemberSubscriptionPlanRequest(
	@NotNull
	Long planId
) {
}
