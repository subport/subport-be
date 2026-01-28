package subport.application.token.port.out;

import subport.domain.token.RefreshToken;

public interface DeleteRefreshTokenPort {

	void delete(String refreshTokenValue);

	void delete(RefreshToken refreshToken);
}
