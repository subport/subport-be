package subport.application.token.port.out;

import subport.domain.token.RefreshToken;

public interface LoadRefreshTokenPort {

	RefreshToken load(String tokenValue);
}
