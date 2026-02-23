package subport.admin.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.auth.RefreshTokenPort;
import subport.admin.application.exception.AdminErrorCode;
import subport.common.exception.CustomException;
import subport.domain.token.RefreshToken;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenPort {

	private final SpringDataRefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public RefreshToken load(String tokenValue) {
		return refreshTokenRepository.findByTokenValue(tokenValue)
			.orElseThrow(() -> new CustomException(AdminErrorCode.REFRESH_TOKEN_NOT_FOUND));
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
