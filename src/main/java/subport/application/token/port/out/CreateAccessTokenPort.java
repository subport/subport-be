package subport.application.token.port.out;

import java.time.Instant;

public interface CreateAccessTokenPort {

	String createAccessToken(Long memberId, Instant now);
}
