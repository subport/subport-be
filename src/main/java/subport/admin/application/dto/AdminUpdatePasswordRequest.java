package subport.admin.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AdminUpdatePasswordRequest(
	@NotBlank
	String oldPassword,

	@NotBlank
	@Pattern(
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d])[\\x21-\\x7E]{12,}$",
		message = "비밀번호는 영문, 숫자, 특수문자를 포함한 12자 이상이어야 합니다."
	)
	String newPassword,

	@NotBlank
	String confirmPassword
) {
}
