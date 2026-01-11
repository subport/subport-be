package subport.adapter.out.persistence.membersubscription;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.subscription.PlanJpaEntity;
import subport.adapter.out.persistence.subscription.SubscriptionJpaEntity;

@Entity
@Table(name = "member_subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSubscriptionJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate startDate;

	private Integer reminderDaysBeforeEnd;

	private String memo;

	private boolean dutchPay;

	private BigDecimal dutchPayAmount;

	private boolean active;

	private LocalDate terminationDate;

	private LocalDate nextPaymentDate;

	@JoinColumn(name = "member_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	@JoinColumn(name = "subscription_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private SubscriptionJpaEntity subscription;

	@JoinColumn(name = "subscription_plan_id", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private PlanJpaEntity subscriptionPlan;

	public MemberSubscriptionJpaEntity(
		LocalDate startDate,
		Integer reminderDaysBeforeEnd,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		boolean active,
		LocalDate terminationDate,
		LocalDate nextPaymentDate,
		MemberJpaEntity member,
		SubscriptionJpaEntity subscription,
		PlanJpaEntity subscriptionPlan
	) {
		this.startDate = startDate;
		this.reminderDaysBeforeEnd = reminderDaysBeforeEnd;
		this.memo = memo;
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
		this.active = active;
		this.terminationDate = terminationDate;
		this.nextPaymentDate = nextPaymentDate;
		this.member = member;
		this.subscription = subscription;
		this.subscriptionPlan = subscriptionPlan;
	}
}
