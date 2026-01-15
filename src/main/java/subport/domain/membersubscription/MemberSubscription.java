package subport.domain.membersubscription;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class MemberSubscription {

	private final Long id;

	private final LocalDate startDate;

	private Integer reminderDaysBefore;

	private String memo;

	private boolean dutchPay;

	private BigDecimal dutchPayAmount;

	private final boolean active;

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
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		this.id = id;
		this.startDate = startDate;
		this.reminderDaysBefore = reminderDaysBefore;
		this.memo = memo;
		this.dutchPay = dutchPay;
		if (dutchPay) {
			this.dutchPayAmount = dutchPayAmount;
		} else {
			this.dutchPayAmount = null;
		}
		this.active = true;
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

	public void increaseNextPaymentDateByMonths(int months) {
		this.nextPaymentDate = this.nextPaymentDate.plusMonths(months);
	}
}
