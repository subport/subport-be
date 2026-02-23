package subport.api.application.auth.port.out;

import subport.domain.token.RefreshToken;

public interface SaveRefreshTokenPort {

	void save(RefreshToken refreshToken);
}
