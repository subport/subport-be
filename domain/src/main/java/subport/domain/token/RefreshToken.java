package subport.domain.token;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token", uniqueConstraints = {
	@UniqueConstraint(columnNames = "token_value")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tokenValue;

	private Long subjectId;

	@Enumerated(EnumType.STRING)
	private RefreshTokenRole role;

	private Instant issuedAt;

	private Instant expiration;

	public RefreshToken(
		String tokenValue,
		Long subjectId,
		RefreshTokenRole role,
		Instant issuedAt,
		Instant expiration
	) {
		this.tokenValue = tokenValue;
		this.subjectId = subjectId;
		this.role = role;
		this.issuedAt = issuedAt;
		this.expiration = expiration;
	}

	public boolean isExpired(Instant now) {
		return !expiration.isAfter(now);
	}
}
