package subport.application.token.port.in;

import java.time.Instant;

import subport.application.token.port.in.dto.TokenPair;

public interface ReissueTokenUseCase {

	TokenPair reissue(String refreshTokenValue, Instant currentInstant);
}
