package subport.api.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.auth.port.out.DeleteRefreshTokenPort;
import subport.api.application.auth.port.out.LoadRefreshTokenPort;
import subport.api.application.auth.port.out.SaveRefreshTokenPort;
import subport.api.application.exception.ApiErrorCode;
import subport.common.exception.CustomException;
import subport.domain.token.RefreshToken;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements
	SaveRefreshTokenPort,
	LoadRefreshTokenPort,
	DeleteRefreshTokenPort {

	private final SpringDataRefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	@Override
	public RefreshToken load(String tokenValue) {
		return refreshTokenRepository.findByTokenValue(tokenValue)
			.orElseThrow(() -> new CustomException(ApiErrorCode.REFRESH_TOKEN_NOT_FOUND));
	}

	@Override
	public void delete(String refreshTokenValue) {
		refreshTokenRepository.deleteByTokenValue(refreshTokenValue);
	}

	@Override
	public void delete(RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}

	@Override
	public void deleteByMemberId(Long memberId) {
		refreshTokenRepository.deleteBySubjectId(memberId);
	}
}
