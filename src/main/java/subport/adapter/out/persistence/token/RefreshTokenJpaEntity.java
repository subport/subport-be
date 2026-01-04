package subport.adapter.out.persistence.token;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	private Long memberId;

	private Date issuedAt;

	private Date expiration;

	public RefreshTokenJpaEntity(
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
