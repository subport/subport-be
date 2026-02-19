package subport.application.token.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.token.port.in.IssueTokenUseCase;
import subport.application.token.port.in.dto.TokenPair;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.CreateRefreshTokenPort;
import subport.application.token.port.out.SaveRefreshTokenPort;
import subport.domain.token.RefreshToken;
import subport.domain.token.Role;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {

	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final SaveRefreshTokenPort saveRefreshTokenPort;

	@Override
	public TokenPair issue(Long memberId, Instant currentInstant) {
		String accessToken = createAccessTokenPort.createAccessToken(memberId, currentInstant, Role.USER);

		RefreshToken refreshToken = createRefreshTokenPort.createRefreshToken(memberId, currentInstant, Role.USER);
		saveRefreshTokenPort.save(refreshToken);

		return new TokenPair(accessToken, refreshToken.getTokenValue());
	}
}
