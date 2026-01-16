package subport.application.token.port.in;

import subport.application.token.port.in.dto.TokenPair;

public interface IssueTokenUseCase {

	TokenPair issue(Long memberId);
}
