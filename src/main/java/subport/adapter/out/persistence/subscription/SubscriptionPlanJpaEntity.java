package subport.adapter.out.persistence.subscription;

import java.math.BigDecimal;

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
import subport.domain.subscription.SubscriptionAmountUnit;
import subport.domain.subscription.SubscriptionPlan;

@Entity
@Table(name = "subscription_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubscriptionPlanJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String planName;

	private BigDecimal amount;

	@Enumerated(value = EnumType.STRING)
	private SubscriptionAmountUnit amountUnit;

	private int durationMonths;

	private boolean systemProvided;

	@JoinColumn(name = "member_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	@JoinColumn(name = "subscription_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private SubscriptionJpaEntity subscription;

	public SubscriptionPlanJpaEntity(
		String planName,
		BigDecimal amount,
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

	public void apply(SubscriptionPlan subscriptionPlan) {
		this.planName = subscriptionPlan.getPlanName();
		this.amount = subscriptionPlan.getAmount();
		this.amountUnit = subscriptionPlan.getAmountUnit();
		this.durationMonths = subscriptionPlan.getDurationMonths();
	}
}
