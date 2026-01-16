package subport.application.token.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exception.RefreshTokenExpiredException;
import subport.application.token.port.in.ReissueTokenUseCase;
import subport.application.token.port.out.CreateAccessTokenPort;
import subport.application.token.port.out.DeleteRefreshTokenPort;
import subport.application.token.port.out.LoadRefreshTokenPort;
import subport.application.token.port.out.ReissueTokenResponse;
import subport.domain.token.RefreshToken;

@Service
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {

	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;
	private final CreateAccessTokenPort createAccessTokenPort;

	@Transactional
	@Override
	public ReissueTokenResponse reissue(String refreshTokenValue) {
		if (refreshTokenValue == null) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		RefreshToken refreshToken = loadRefreshTokenPort.load(refreshTokenValue);
		if (refreshToken.isExpired(Instant.now())) {
			deleteRefreshTokenPort.delete(refreshToken.getId());
			throw new RefreshTokenExpiredException();
		}

		return new ReissueTokenResponse(
			createAccessTokenPort.createAccessToken(
				refreshToken.getMemberId(),
				Instant.now())
		);
	}
}
