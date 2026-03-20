package subport.api.application.auth.port.in;

import java.time.Instant;

import subport.common.jwt.dto.AccessTokenResponse;

public interface GuestLoginUseCase {

	AccessTokenResponse login(Instant now);
}
