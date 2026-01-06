package subport.domain.membersubscription;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;

@Getter
public class MemberSubscription {

	private final Long id;

	private final LocalDate startDate;

	private final Integer reminderDaysBeforeEnd;

	private final String memo;

	private final boolean dutchPay;

	private final BigDecimal dutchPayAmount;

	private final boolean active;

	private final LocalDate terminationDate;

	private final LocalDate nextPaymentDate;

	private final Long memberId;

	private final Long subscriptionId;

	private final Long subscriptionPlanId;

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
		Long subscriptionPlanId
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
		this.subscriptionPlanId = subscriptionPlanId;
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
		Long subscriptionPlanId
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
			subscriptionPlanId
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
		Long subscriptionPlanId
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
			subscriptionPlanId
		);
	}
}
