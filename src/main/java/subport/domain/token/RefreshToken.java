package subport.domain.token;

import java.time.Instant;

import lombok.Getter;

@Getter
public class RefreshToken {

	private String tokenValue;

	private Long memberId;

	private Instant issuedAt;

	private Instant expiration;

	public RefreshToken(
		String tokenValue,
		Long memberId,
		Instant issuedAt,
		Instant expiration
	) {
		this.tokenValue = tokenValue;
		this.memberId = memberId;
		this.issuedAt = issuedAt;
		this.expiration = expiration;
	}
}
