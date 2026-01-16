package subport.application.token.port.in;

public interface ReissueTokenUseCase {

	TokenPair reissue(String refreshTokenValue);
}
