package subport.api.application.auth.service;

import java.time.Instant;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.auth.port.in.ReissueTokenUseCase;
import subport.api.application.auth.port.out.CreateAccessTokenPort;
import subport.api.application.auth.port.out.CreateRefreshTokenPort;
import subport.api.application.auth.port.out.DeleteRefreshTokenPort;
import subport.api.application.auth.port.out.LoadRefreshTokenPort;
import subport.api.application.auth.port.out.SaveRefreshTokenPort;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.common.exception.CustomException;
import subport.common.exception.RefreshTokenExpiredException;
import subport.common.jwt.dto.TokenPair;
import subport.domain.member.Member;
import subport.domain.token.RefreshToken;
import subport.domain.token.Role;

@Service
@Transactional
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final SaveRefreshTokenPort saveRefreshTokenPort;
	private final LoadMemberPort loadMemberPort;

	@Override
	public TokenPair reissue(String refreshTokenValue, Instant currentInstant) {
		if (refreshTokenValue == null) {
			throw new CustomException(ApiErrorCode.REFRESH_TOKEN_REQUIRED);
		}

		RefreshToken refreshToken = loadRefreshTokenPort.load(refreshTokenValue);
		if (refreshToken.isExpired(currentInstant)) {
			deleteRefreshTokenPort.delete(refreshToken);
			throw new RefreshTokenExpiredException(ApiErrorCode.REFRESH_TOKEN_EXPIRED);
		}

		Long memberId = refreshToken.getSubjectId();
		String accessToken = createAccessTokenPort.createAccessToken(memberId, currentInstant, Role.USER);

		deleteRefreshTokenPort.delete(refreshToken);
		refreshToken = createRefreshTokenPort.createRefreshToken(memberId, currentInstant, Role.USER);
		saveRefreshTokenPort.save(refreshToken);

		Member member = loadMemberPort.load(memberId);
		member.updateLastActiveAt(
			currentInstant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime()
		);

		return new TokenPair(accessToken, refreshToken.getTokenValue());
	}
}
