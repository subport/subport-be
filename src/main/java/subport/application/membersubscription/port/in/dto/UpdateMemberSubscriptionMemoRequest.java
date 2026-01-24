package subport.application.membersubscription.port.in.dto;

import jakarta.validation.constraints.Size;

public record UpdateMemberSubscriptionMemoRequest(
	@Size(max = 100)
	String memo
) {
}
