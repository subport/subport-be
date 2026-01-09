package subport.adapter.out.persistence.subscription;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Entity
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

	@JoinColumn(name = "member_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	public SubscriptionJpaEntity(
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

	public void apply(Subscription subscription) {
		this.name = subscription.getName();
		this.type = subscription.getType();
		this.logoImageUrl = subscription.getLogoImageUrl();
		this.planUrl = subscription.getPlanUrl();
	}
}
