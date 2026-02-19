package subport.admin.application.dto;

public record AdminLoginRequest(
	String email,
	String password
) {
}
