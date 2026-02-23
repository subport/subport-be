package subport.api.application.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.auth.port.in.LogoutUseCase;
import subport.api.application.auth.port.out.DeleteRefreshTokenPort;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.CustomException;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

	private final DeleteRefreshTokenPort deleteRefreshTokenPort;

	@Override
	public void logout(String refreshTokenValue) {
		if (refreshTokenValue == null) {
			throw new CustomException(ApiErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		deleteRefreshTokenPort.delete(refreshTokenValue);
	}
}
