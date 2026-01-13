package subport.adapter.out.persistence.token;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String value;

	private Long memberId;

	private Instant issuedAt;

	private Instant expiration;

	public RefreshTokenJpaEntity(
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
