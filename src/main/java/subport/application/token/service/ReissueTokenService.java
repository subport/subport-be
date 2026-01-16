package subport.application.token.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exception.RefreshTokenExpiredException;
import subport.application.token.port.in.ReissueTokenUseCase;
import subport.application.token.port.in.TokenPair;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.CreateRefreshTokenPort;
import subport.application.token.port.out.DeleteRefreshTokenPort;
import subport.application.token.port.out.LoadRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Service
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;

	@Transactional
	@Override
	public TokenPair reissue(String refreshTokenValue) {
		if (refreshTokenValue == null) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		RefreshToken refreshToken = loadRefreshTokenPort.load(refreshTokenValue);
		Instant now = Instant.now();
		if (refreshToken.isExpired(now)) {
			deleteRefreshTokenPort.delete(refreshToken.getId());
			throw new RefreshTokenExpiredException();
		}

		Long memberId = refreshToken.getMemberId();
		String accessToken = createAccessTokenPort.createAccessToken(memberId, now);
		refreshToken = createRefreshTokenPort.createRefreshToken(memberId, now);

		return new TokenPair(accessToken, refreshToken.getTokenValue());
	}
}
