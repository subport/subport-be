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

	private Integer reminderDaysBefore;

	private String memo;

	private boolean dutchPay;

	private BigDecimal dutchPayAmount;

	private BigDecimal exchangeRate;

	private LocalDate exchangeRateDate;

	private boolean active;

	private LocalDate nextPaymentDate;

	@JoinColumn(name = "member_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberJpaEntity member;

	@JoinColumn(name = "subscription_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private SubscriptionJpaEntity subscription;

	@JoinColumn(name = "plan_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private PlanJpaEntity plan;

	public MemberSubscriptionJpaEntity(
		LocalDate startDate,
		Integer reminderDaysBefore,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		BigDecimal exchangeRate,
		LocalDate exchangeRateDate,
		boolean active,
		LocalDate nextPaymentDate,
		MemberJpaEntity member,
		SubscriptionJpaEntity subscription,
		PlanJpaEntity plan
	) {
		this.startDate = startDate;
		this.reminderDaysBefore = reminderDaysBefore;
		this.memo = memo;
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
		this.exchangeRate = exchangeRate;
		this.exchangeRateDate = exchangeRateDate;
		this.active = active;
		this.nextPaymentDate = nextPaymentDate;
		this.member = member;
		this.subscription = subscription;
		this.plan = plan;
	}

	public void updateReminderDaysBefore(Integer reminderDaysBefore) {
		this.reminderDaysBefore = reminderDaysBefore;
	}

	public void updateMemo(String memo) {
		this.memo = memo;
	}

	public void updateDutchPay(boolean dutchPay, BigDecimal dutchPayAmount) {
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
	}

	public void updateActive(boolean active) {
		this.active = active;
	}

	public void updateNextPaymentDate(LocalDate nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public void updatePlan(PlanJpaEntity plan) {
		this.plan = plan;
	}
}
