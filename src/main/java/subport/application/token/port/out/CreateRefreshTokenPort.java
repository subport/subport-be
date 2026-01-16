package subport.application.token.port.out;

import java.time.Instant;

import subport.domain.token.RefreshToken;

public interface CreateRefreshTokenPort {

	RefreshToken createRefreshToken(Long memberId, Instant now);
}
