package subport.application.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.token.port.in.LogoutUseCase;
import subport.application.token.port.out.DeleteRefreshTokenPort;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

	private final DeleteRefreshTokenPort deleteRefreshTokenPort;

	@Override
	public void logout(String refreshTokenValue) {
		if (refreshTokenValue == null) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_NULL);
		}

		deleteRefreshTokenPort.delete(refreshTokenValue);
	}
}
