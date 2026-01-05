package subport.adapter.out.persistence.subscription;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.SubscriptionType;

@Entity
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(value = EnumType.STRING)
	private SubscriptionType type;

	private String logoImageUrl;

	private String planUrl;

	private boolean systemProvided;

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	private SubscriptionJpaEntity(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		boolean systemProvided,
		MemberJpaEntity member
	) {
		this.name = name;
		this.type = type;
		this.logoImageUrl = logoImageUrl;
		this.planUrl = planUrl;
		this.systemProvided = systemProvided;
		this.member = member;
	}

	public static SubscriptionJpaEntity customSubscription(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		MemberJpaEntity member
	) {
		return new SubscriptionJpaEntity(
			name,
			type,
			logoImageUrl,
			planUrl,
			false,
			member
		);
	}

	public static SubscriptionJpaEntity systemSubscription(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		MemberJpaEntity member
	) {
		return new SubscriptionJpaEntity(
			name,
			type,
			logoImageUrl,
			planUrl,
			true,
			null
		);
	}
}
