package subport.application.token.port.out;

import java.time.Instant;

import subport.domain.token.Role;

public interface CreateAccessTokenPort {

	String createAccessToken(
		Long memberId,
		Instant now,
		Role role
	);
}
