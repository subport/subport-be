package subport.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
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
	DeleteRefreshTokenPort {

	private final SpringDataRefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	public void save(RefreshToken refreshToken) {
		RefreshTokenJpaEntity refreshTokenEntity = refreshTokenMapper.toJpaEntity(refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);
	}

	@Override
	public RefreshToken load(String tokenValue) {
		RefreshTokenJpaEntity refreshTokenEntity = refreshTokenRepository.findByTokenValue(tokenValue)
			.orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

		return refreshTokenMapper.toDomain(refreshTokenEntity);
	}

	@Override
	public void delete(String tokenValue) {
		refreshTokenRepository.deleteByTokenValue(tokenValue);
	}
}
