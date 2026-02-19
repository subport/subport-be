package subport.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminRefreshTokenPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.token.port.out.DeleteRefreshTokenPort;
import subport.application.token.port.out.LoadRefreshTokenPort;
import subport.application.token.port.out.SaveRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements
	SaveRefreshTokenPort,
	LoadRefreshTokenPort,
	DeleteRefreshTokenPort,
	AdminRefreshTokenPort {

	private final SpringDataRefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public RefreshToken load(String tokenValue) {
		return refreshTokenRepository.findByTokenValue(tokenValue)
			.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
	}

	@Override
	public void delete(String refreshTokenValue) {
		refreshTokenRepository.deleteByTokenValue(refreshTokenValue);
	}

	@Override
	public void delete(RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}
}
