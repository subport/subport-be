package subport.domain.token;

import java.time.Instant;

import lombok.Getter;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Getter
public class RefreshToken {

	private Long id;

	private String tokenValue;

	private Long memberId;

	private Instant issuedAt;

	private Instant expiration;

	private RefreshToken(
		Long id,
		String tokenValue,
		Long memberId,
		Instant issuedAt,
		Instant expiration
	) {
		this.id = id;
		this.tokenValue = tokenValue;
		this.memberId = memberId;
		this.issuedAt = issuedAt;
		this.expiration = expiration;
	}

	public static RefreshToken withId(
		Long id,
		String tokenValue,
		Long memberId,
		Instant issuedAt,
		Instant expiration
	) {
		return new RefreshToken(
			id,
			tokenValue,
			memberId,
			issuedAt,
			expiration
		);
	}

	public static RefreshToken withoutId(
		String tokenValue,
		Long memberId,
		Instant issuedAt,
		Instant expiration
	) {
		return new RefreshToken(
			null,
			tokenValue,
			memberId,
			issuedAt,
			expiration
		);
	}

	public void verifyExpiration(Instant now) {
		if (!expiration.isAfter(now)) {
			throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}
	}
}
