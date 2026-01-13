package subport.application.token.port.in;

import subport.application.token.port.out.ReissueTokenResponse;

public interface ReissueTokenUseCase {

	ReissueTokenResponse reissue(String refreshTokenValue);
}
