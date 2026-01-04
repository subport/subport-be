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

@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPlanJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String subscriptionName;

	private String planName;

	private int amount;

	private int durationMonths;

	private boolean isDefault;

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	public SubscriptionPlanJpaEntity(
		String subscriptionName,
		String planName,
		int amount,
		int durationMonths,
		MemberJpaEntity member
	) {
		this.subscriptionName = subscriptionName;
		this.planName = planName;
		this.amount = amount;
		this.durationMonths = durationMonths;
		this.isDefault = false;
		this.member = member;
	}
}
