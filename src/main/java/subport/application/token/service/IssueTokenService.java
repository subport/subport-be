package subport.application.token.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.token.port.in.IssueTokenUseCase;
import subport.application.token.port.in.dto.TokenPair;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.CreateRefreshTokenPort;
import subport.application.token.port.out.SaveRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Service
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {

	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final SaveRefreshTokenPort saveRefreshTokenPort;

	@Override
	public TokenPair issue(Long memberId) {
		Instant now = Instant.now();
		String accessToken = createAccessTokenPort.createAccessToken(memberId, now);

		RefreshToken refreshToken = createRefreshTokenPort.createRefreshToken(memberId, now);
		saveRefreshTokenPort.save(refreshToken);

		return new TokenPair(accessToken, refreshToken.getTokenValue());
	}
}
