package subport.domain.membersubscription;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class MemberSubscription {

	private final Long id;

	private final LocalDate startDate;

	private Integer reminderDaysBeforeEnd;

	private final String memo;

	private boolean dutchPay;

	private BigDecimal dutchPayAmount;

	private final boolean active;

	private final LocalDate terminationDate;

	private final LocalDate nextPaymentDate;

	private final Long memberId;

	private final Long subscriptionId;

	private Long planId;

	private MemberSubscription(
		Long id,
		LocalDate startDate,
		Integer reminderDaysBeforeEnd,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		LocalDate terminationDate,
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		this.id = id;
		this.startDate = startDate;
		this.reminderDaysBeforeEnd = reminderDaysBeforeEnd;
		this.memo = memo;
		this.dutchPay = dutchPay;
		if (dutchPay) {
			this.dutchPayAmount = dutchPayAmount;
		} else {
			this.dutchPayAmount = null;
		}
		this.active = true;
		this.terminationDate = terminationDate;
		this.nextPaymentDate = nextPaymentDate;
		this.memberId = memberId;
		this.subscriptionId = subscriptionId;
		this.planId = planId;
	}

	public static MemberSubscription withId(
		Long id,
		LocalDate startDate,
		Integer reminderDaysBeforeEnd,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		LocalDate terminationDate,
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		return new MemberSubscription(
			id,
			startDate,
			reminderDaysBeforeEnd,
			memo,
			dutchPay,
			dutchPayAmount,
			terminationDate,
			nextPaymentDate,
			memberId,
			subscriptionId,
			planId
		);
	}

	public static MemberSubscription withoutId(
		LocalDate startDate,
		Integer reminderDaysBeforeEnd,
		String memo,
		boolean dutchPay,
		BigDecimal dutchPayAmount,
		LocalDate terminationDate,
		LocalDate nextPaymentDate,
		Long memberId,
		Long subscriptionId,
		Long planId
	) {
		return new MemberSubscription(
			null,
			startDate,
			reminderDaysBeforeEnd,
			memo,
			dutchPay,
			dutchPayAmount,
			terminationDate,
			nextPaymentDate,
			memberId,
			subscriptionId,
			planId
		);
	}

	public void updatePlan(Long planId) {
		this.planId = planId;
	}

	public void updateDutchPay(boolean dutchPay, BigDecimal dutchPayAmount) {
		this.dutchPay = dutchPay;
		this.dutchPayAmount = dutchPayAmount;
	}

	public void updateReminderDaysBeforeEnd(Integer reminderDaysBeforeEnd) {
		this.reminderDaysBeforeEnd = reminderDaysBeforeEnd;
	}
}
