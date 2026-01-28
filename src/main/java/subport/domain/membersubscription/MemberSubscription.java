package subport.domain.membersubscription;

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
import subport.domain.member.Member;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Entity
@Table(name = "member_subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSubscription extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate startDate;

	private Integer reminderDaysBefore;

	private LocalDate reminderDate;

	private String memo;

	private boolean dutchPay;

	private BigDecimal dutchPayAmount;

	private BigDecimal exchangeRate;

	private LocalDate exchangeRateDate;

	private boolean active;

	private LocalDate lastPaymentDate;

	private LocalDate nextPaymentDate;

	@JoinColumn(name = "member_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "subscription_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Subscription subscription;

	@JoinColumn(name = "plan_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Plan plan;

	public MemberSubscription(
		LocalDate startDate,
		Integer reminderDaysBefore,
		LocalDate reminderDate,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		BigDecimal exchangeRate,
		LocalDate exchangeRateDate,
		LocalDate lastPaymentDate,
		LocalDate nextPaymentDate,
		Member member,
		Subscription subscription,
		Plan plan
	) {
		this.startDate = startDate;
		this.reminderDaysBefore = reminderDaysBefore;
		this.reminderDate = reminderDate;
		this.memo = memo;
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
		this.exchangeRate = exchangeRate;
		this.exchangeRateDate = exchangeRateDate;
		this.active = true;
		this.lastPaymentDate = lastPaymentDate;
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

	public void updatePlan(Plan plan) {
		this.plan = plan;
	}

	public void updateDutchPay(boolean dutchPay, BigDecimal dutchPayAmount) {
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
	}

	public void updateExchangeRate(BigDecimal exchangeRate, LocalDate exchangeRateDate) {
		this.exchangeRate = exchangeRate;
		this.exchangeRateDate = exchangeRateDate;
	}

	public void updateLastPaymentDate(LocalDate lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public void updateNextPaymentDate(LocalDate nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}

	public void increaseNextPaymentDateByMonths(long months) {
		this.nextPaymentDate = this.nextPaymentDate.plusMonths(months);
	}

	public void deactivate() {
		this.active = false;
	}
}
