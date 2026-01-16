package subport.application.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.token.port.in.LogoutUseCase;
import subport.application.token.port.out.DeleteRefreshTokenPort;
import subport.application.token.port.out.LoadRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;

	@Transactional
	@Override
	public void logout(String refreshTokenValue) {
		if (refreshTokenValue == null) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		RefreshToken refreshToken = loadRefreshTokenPort.load(refreshTokenValue);

		deleteRefreshTokenPort.delete(refreshToken.getId());
	}
}
