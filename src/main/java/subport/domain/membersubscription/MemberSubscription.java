package subport.domain.membersubscription;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class MemberSubscription {

	private final Long id;

	private final LocalDate startDate;

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

	private final Long memberId;

	private final Long subscriptionId;

	private Long planId;

	private MemberSubscription(
		Long id,
		LocalDate startDate,
		Integer reminderDaysBefore,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		BigDecimal exchangeRate,
		LocalDate exchangeRateDate,
		boolean active,
		LocalDate lastPaymentDate,
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		this.id = id;
		this.startDate = startDate;
		this.reminderDaysBefore = reminderDaysBefore;
		this.reminderDate = nextPaymentDate.minusDays(reminderDaysBefore);
		this.memo = memo;
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
		this.exchangeRate = exchangeRate;
		this.exchangeRateDate = exchangeRateDate;
		this.active = active;
		this.lastPaymentDate = lastPaymentDate;
		this.nextPaymentDate = nextPaymentDate;
		this.memberId = memberId;
		this.subscriptionId = subscriptionId;
		this.planId = planId;
	}

	public static MemberSubscription withId(
		Long id,
		LocalDate startDate,
		Integer reminderDaysBefore,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		BigDecimal exchangeRate,
		LocalDate exchangeRateDate,
		boolean active,
		LocalDate lastPaymentDate,
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		return new MemberSubscription(
			id,
			startDate,
			reminderDaysBefore,
			memo,
			dutchPay,
			dutchPayAmount,
			exchangeRate,
			exchangeRateDate,
			active,
			lastPaymentDate,
			nextPaymentDate,
			memberId,
			subscriptionId,
			planId
		);
	}

	public static MemberSubscription withoutId(
		LocalDate startDate,
		Integer reminderDaysBefore,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		BigDecimal exchangeRate,
		LocalDate exchangeRateDate,
		boolean active,
		LocalDate lastPaymentDate,
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		return new MemberSubscription(
			null,
			startDate,
			reminderDaysBefore,
			memo,
			dutchPay,
			dutchPayAmount,
			exchangeRate,
			exchangeRateDate,
			active,
			lastPaymentDate,
			nextPaymentDate,
			memberId,
			subscriptionId,
			planId
		);
	}

	public void updateReminderDaysBefore(Integer reminderDaysBefore) {
		this.reminderDaysBefore = reminderDaysBefore;
	}

	public void updateMemo(String memo) {
		this.memo = memo;
	}

	public void updatePlan(Long planId) {
		this.planId = planId;
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
