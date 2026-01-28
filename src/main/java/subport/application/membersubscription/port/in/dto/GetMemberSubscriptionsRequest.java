package subport.application.membersubscription.port.in.dto;

import jakarta.validation.constraints.NotBlank;

public record GetMemberSubscriptionsRequest(
	boolean active,

	@NotBlank
	String sortBy
) {
}
