package subport.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import subport.domain.token.RefreshToken;

@Component
public class RefreshTokenMapper {

	public RefreshTokenJpaEntity toJpaEntity(RefreshToken refreshToken) {
		return new RefreshTokenJpaEntity(
			refreshToken.getTokenValue(),
			refreshToken.getMemberId(),
			refreshToken.getIssuedAt(),
			refreshToken.getExpiration()
		);
	}

	public RefreshToken toDomain(RefreshTokenJpaEntity refreshTokenEntity) {
		return new RefreshToken(
			refreshTokenEntity.getTokenValue(),
			refreshTokenEntity.getMemberId(),
			refreshTokenEntity.getIssuedAt(),
			refreshTokenEntity.getExpiration()
		);
	}
}
