package subport.admin.application.account.dto;

public record LoginAdminRequest(
	String email,
	String password
) {
}
