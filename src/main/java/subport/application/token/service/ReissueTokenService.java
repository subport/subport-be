package subport.application.token.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exception.RefreshTokenExpiredException;
import subport.application.token.port.in.ReissueTokenUseCase;
import subport.application.token.port.in.dto.TokenPair;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.CreateRefreshTokenPort;
import subport.application.token.port.out.DeleteRefreshTokenPort;
import subport.application.token.port.out.LoadRefreshTokenPort;
import subport.application.token.port.out.SaveRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Service
@Transactional
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final CreateAccessTokenPort createAccessTokenPort;
	private final CreateRefreshTokenPort createRefreshTokenPort;
	private final SaveRefreshTokenPort saveRefreshTokenPort;

	@Override
	public TokenPair reissue(String refreshTokenValue, Instant currentInstant) {
		if (refreshTokenValue == null) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		RefreshToken refreshToken = loadRefreshTokenPort.load(refreshTokenValue);
		if (refreshToken.isExpired(currentInstant)) {
			deleteRefreshTokenPort.delete(refreshToken);
			throw new RefreshTokenExpiredException();
		}

		Long memberId = refreshToken.getMemberId();
		String accessToken = createAccessTokenPort.createAccessToken(memberId, currentInstant);

		deleteRefreshTokenPort.delete(refreshToken);
		refreshToken = createRefreshTokenPort.createRefreshToken(memberId, currentInstant);
		saveRefreshTokenPort.save(refreshToken);

		return new TokenPair(accessToken, refreshToken.getTokenValue());
	}
}
