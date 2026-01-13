package subport.adapter.out.persistence.token;

import org.springframework.stereotype.Component;

import subport.domain.token.RefreshToken;

@Component
public class RefreshTokenMapper {

	public RefreshTokenJpaEntity toJpaEntity(RefreshToken refreshToken) {
		return new RefreshTokenJpaEntity(
			refreshToken.getValue(),
			refreshToken.getMemberId(),
			refreshToken.getIssuedAt(),
			refreshToken.getExpiration()
		);
	}
}
