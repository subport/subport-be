package subport.application.member.port.in.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberRequest(
	@NotBlank
	@Size(max = 10)
	String nickname,

	@Email
	String email
) {
}
