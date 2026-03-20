package subport.api.application.auth.port.out;

import java.time.Instant;

import subport.domain.member.MemberRole;

public interface CreateAccessTokenPort {

	String createAccessToken(
		Long subjectId,
		Instant now,
		MemberRole role
	);
}
