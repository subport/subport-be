package subport.api.application.auth.port.out;

import subport.domain.token.RefreshToken;

public interface LoadRefreshTokenPort {

	RefreshToken load(String tokenValue);
}
