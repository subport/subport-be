package subscribe.application.token.port.in;

import subscribe.domain.token.RefreshToken;

public interface SaveRefreshTokenUseCase {

	void saveRefreshToken(RefreshToken refreshToken);
}
