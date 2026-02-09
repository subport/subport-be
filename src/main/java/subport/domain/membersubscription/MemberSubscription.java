package subport.domain.membersubscription;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import subport.domain.subscription.AmountUnit;
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

	private String memo;

	private boolean dutchPay;

	private BigDecimal dutchPayAmount;

	private BigDecimal exchangeRate;

	private LocalDate exchangeRateDate;

	private boolean active;

	private LocalDate lastPaymentDate;

	private LocalDate nextPaymentDate;

	private LocalDate paymentReminderDate;

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
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		BigDecimal exchangeRate,
		LocalDate exchangeRateDate,
		Member member,
		Subscription subscription,
		Plan plan
	) {
		this.startDate = startDate;
		this.memo = memo;
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
		this.exchangeRate = exchangeRate;
		this.exchangeRateDate = exchangeRateDate;
		this.member = member;
		this.subscription = subscription;
		this.plan = plan;
		active = true;
		lastPaymentDate = startDate;
		nextPaymentDate = plan.calculateNextPaymentDate(startDate);
		updatePaymentReminderDate();
	}

	public void updateMemo(String memo) {
		this.memo = memo;
	}

	public void updatePlan(Plan plan) {
		this.plan = plan;
		nextPaymentDate = plan.calculateNextPaymentDate(lastPaymentDate);
		updatePaymentReminderDate();
	}

	public void updateDutchPay(boolean dutchPay, BigDecimal dutchPayAmount) {
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
	}

	public void updateExchangeRate(BigDecimal exchangeRate, LocalDate exchangeRateDate) {
		this.exchangeRate = exchangeRate;
		this.exchangeRateDate = exchangeRateDate;
	}

	public void updateLastPaymentDate() {
		lastPaymentDate = nextPaymentDate;
		nextPaymentDate = plan.calculateNextPaymentDate(nextPaymentDate);
		updatePaymentReminderDate();
	}

	public void deactivate() {
		active = false;
	}

	public void activate(LocalDate startDate) {
		this.startDate = startDate;
		lastPaymentDate = startDate;
		nextPaymentDate = plan.calculateNextPaymentDate(startDate);
		updatePaymentReminderDate();
	}

	public BigDecimal calculateActualPaymentAmount() {
		AmountUnit amountUnit = plan.getAmountUnit();
		BigDecimal planAmount = plan.getAmount();

		if (dutchPay) {
			return dutchPayAmount;
		}
		if (amountUnit.equals(AmountUnit.USD) && exchangeRate != null) {
			return planAmount.multiply(exchangeRate)
				.setScale(0, RoundingMode.HALF_UP);
		}

		return planAmount;
	}

	public boolean isExchangeRateApplicable() {
		return this.exchangeRate != null
			&& this.exchangeRateDate != null
			&& this.plan.isUsdBased();
	}

	public void updatePaymentReminderDate() {
		paymentReminderDate = member.calculatePaymentReminderDate(nextPaymentDate);
	}
}
