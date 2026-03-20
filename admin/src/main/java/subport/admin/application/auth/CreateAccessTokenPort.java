package subport.admin.application.auth;

import java.time.Instant;

public interface CreateAccessTokenPort {

	String createAccessToken(
		Long subjectId,
		Instant now
	);
}
