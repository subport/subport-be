package subport.admin.application.auth;

import java.time.Instant;

import subport.domain.token.RefreshToken;
import subport.domain.token.Role;

public interface CreateRefreshTokenPort {

	RefreshToken createRefreshToken(
		Long subjectId,
		Instant now,
		Role role
	);
}
