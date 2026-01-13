package subport.application.token.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import subport.application.exception.RefreshTokenExpiredException;
import subport.application.token.port.in.ReissueTokenUseCase;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.DeleteRefreshTokenPort;
import subport.application.token.port.out.LoadRefreshTokenPort;
import subport.application.token.port.out.ReissueTokenResponse;
import subport.application.token.port.out.VerifyTokenExpirationPort;
import subport.domain.token.RefreshToken;

@Service
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final VerifyTokenExpirationPort verifyTokenExpirationPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final CreateAccessTokenPort createAccessTokenPort;

	@Transactional
	@Override
	public ReissueTokenResponse reissue(String refreshTokenValue) {
		RefreshToken refreshToken = loadRefreshTokenPort.load(refreshTokenValue);

		refreshTokenValue = refreshToken.getTokenValue();
		try {
			verifyTokenExpirationPort.verifyTokenExpiration(
				refreshTokenValue,
				Instant.now());
		} catch (ExpiredJwtException e) {
			deleteRefreshTokenPort.delete(refreshTokenValue);
			throw new RefreshTokenExpiredException(e);
		}

		return new ReissueTokenResponse(
			createAccessTokenPort.createAccessToken(
				refreshToken.getMemberId(),
				Instant.now())
		);
	}
}
