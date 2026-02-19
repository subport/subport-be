package subport.admin.application.port;

import subport.domain.token.RefreshToken;

public interface AdminRefreshTokenPort {

	void save(RefreshToken token);

	RefreshToken load(String refreshToken);

	void delete(RefreshToken refreshToken);
}
