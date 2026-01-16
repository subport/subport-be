package subport.application.token.port.in;

public interface ValidateAccessTokenUseCase {

	Long validate(String authorizationHeader);
}
