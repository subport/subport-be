package subport.adapter.out.persistence.subscription;

import jakarta.persistence.Entity;
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
import subport.domain.subscription.SubscriptionAmountUnit;

@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPlanJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String planName;

	private int amount;

	private SubscriptionAmountUnit amountUnit;

	private int durationMonths;

	private boolean systemProvided;

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	private SubscriptionJpaEntity subscription;

	public SubscriptionPlanJpaEntity(
		String planName,
		int amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		MemberJpaEntity member,
		SubscriptionJpaEntity subscription
	) {
		this.planName = planName;
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.durationMonths = durationMonths;
		this.systemProvided = systemProvided;
		this.member = member;
		this.subscription = subscription;
	}
}
