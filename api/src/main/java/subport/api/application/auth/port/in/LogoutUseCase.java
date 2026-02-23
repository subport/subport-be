package subport.api.application.auth.port.in;

public interface LogoutUseCase {

	void logout(String refreshTokenValue);
}
