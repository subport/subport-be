package subscribe.adapter.out.persistence.subscription;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subscribe.adapter.out.persistence.BaseTimeEntity;
import subscribe.adapter.out.persistence.member.MemberJpaEntity;

@Entity
@Table(name = "subscription_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionTypeJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private boolean isDefault;

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	public SubscriptionTypeJpaEntity(
		String name,
		MemberJpaEntity member
	) {
		this.name = name;
		this.isDefault = false;
		this.member = member;
	}
}
