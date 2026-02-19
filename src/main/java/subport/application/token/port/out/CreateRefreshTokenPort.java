package subport.application.token.port.out;

import java.time.Instant;

import subport.domain.token.RefreshToken;
import subport.domain.token.Role;

public interface CreateRefreshTokenPort {

	RefreshToken createRefreshToken(
		Long memberId,
		Instant now,
		Role role
	);
}
