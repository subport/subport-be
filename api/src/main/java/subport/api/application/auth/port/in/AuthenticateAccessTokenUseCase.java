package subport.api.application.auth.port.in;

public interface AuthenticateAccessTokenUseCase {

	Long authenticateAndGetMemberId(String authorizationHeader);
}
