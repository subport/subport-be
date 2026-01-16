package subport.application.token.port.in;

public interface IssueTokenUseCase {

	TokenPair issue(Long memberId);
}
