package subport.application.token.port.out;

import subport.domain.token.RefreshToken;

public interface SaveRefreshTokenPort {

	void saveRefreshToken(RefreshToken refreshToken);
}
