package subport.application.token.port.in;

import subport.domain.token.RefreshToken;

public interface SaveRefreshTokenUseCase {

	void save(RefreshToken refreshToken);
}
