package subport.domain.token;

import java.util.Date;

import lombok.Getter;

@Getter
public class RefreshToken {

	private String token;

	private Long memberId;

	private Date issuedAt;

	private Date expiration;

	public RefreshToken(
		String token,
		Long memberId,
		Date issuedAt,
		Date expiration
	) {
		this.token = token;
		this.memberId = memberId;
		this.issuedAt = issuedAt;
		this.expiration = expiration;
	}
}
