package subport.admin.application.auth;

import subport.domain.token.RefreshToken;

public interface RefreshTokenPort {

	void save(RefreshToken token);

	RefreshToken load(String refreshToken);

	void delete(String refreshTokenValue);

	void delete(RefreshToken refreshToken);
}
