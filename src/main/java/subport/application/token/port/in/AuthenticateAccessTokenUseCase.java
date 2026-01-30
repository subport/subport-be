package subport.application.token.port.in;

public interface AuthenticateAccessTokenUseCase {

	Long authenticateAndGetMemberId(String authorizationHeader);
}
