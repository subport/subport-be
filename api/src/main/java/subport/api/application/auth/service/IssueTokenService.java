package subport.api.application.auth.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.auth.port.in.IssueTokenUseCase;
import subport.api.application.auth.port.out.CreateAccessTokenPort;
import subport.api.application.auth.port.out.CreateRefreshTokenPort;
import subport.api.application.auth.port.out.SaveRefreshTokenPort;
import subport.common.jwt.dto.TokenPair;
import subport.domain.member.MemberRole;
import subport.domain.token.RefreshToken;
import subport.domain.token.RefreshTokenRole;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {

	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final SaveRefreshTokenPort saveRefreshTokenPort;

	@Override
	public TokenPair issue(Long memberId, Instant currentInstant) {
		String accessToken = createAccessTokenPort.createAccessToken(memberId, currentInstant, MemberRole.MEMBER);

		RefreshToken refreshToken = createRefreshTokenPort.createRefreshToken(
			memberId,
			currentInstant,
			RefreshTokenRole.MEMBER
		);
		saveRefreshTokenPort.save(refreshToken);

		return new TokenPair(accessToken, refreshToken.getTokenValue());
	}
}
