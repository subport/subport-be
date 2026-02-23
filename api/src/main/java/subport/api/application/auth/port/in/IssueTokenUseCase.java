package subport.api.application.auth.port.in;

import java.time.Instant;

import subport.common.jwt.dto.TokenPair;

public interface IssueTokenUseCase {

	TokenPair issue(Long memberId, Instant currentInstant);
}
