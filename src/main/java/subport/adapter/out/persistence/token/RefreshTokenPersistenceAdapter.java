package subport.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.token.port.out.SaveRefreshTokenPort;
import subport.domain.token.RefreshToken;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements
	SaveRefreshTokenPort {

	private final SpringDataRefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	public void saveRefreshToken(RefreshToken refreshToken) {
		RefreshTokenJpaEntity refreshTokenEntity = refreshTokenMapper.toJpaEntity(refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);
	}
}
