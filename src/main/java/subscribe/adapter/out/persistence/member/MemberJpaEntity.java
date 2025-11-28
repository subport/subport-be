package subscribe.adapter.out.persistence.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subscribe.adapter.out.persistence.BaseTimeEntity;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String providerId;

	private String nickname;

	private String email;

	public MemberJpaEntity(
		String providerId,
		String nickname,
		String email
	) {
		this.providerId = providerId;
		this.nickname = nickname;
		this.email = email;
	}

	public void update(
		String nickname,
		String email
	) {
		this.nickname = nickname;
		this.email = email;
	}
}
