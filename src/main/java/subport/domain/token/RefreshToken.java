package subport.domain.token;

import java.time.Instant;

import lombok.Getter;

@Getter
public class RefreshToken {

	private String value;

	private Long memberId;

	private Instant issuedAt;

	private Instant expiration;

	public RefreshToken(
		String value,
		Long memberId,
		Instant issuedAt,
		Instant expiration
	) {
		this.value = value;
		this.memberId = memberId;
		this.issuedAt = issuedAt;
		this.expiration = expiration;
	}
}
