package subport.domain.token;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	public boolean isExpired(Instant now) {
		return !expiration.isAfter(now);
	}
}
