package subport.domain.subscription;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class SubscriptionPlan {

	private final Long id;

	private final String planName;

	private final BigDecimal amount;

	private final SubscriptionAmountUnit amountUnit;

	private final int durationMonths;

	private final boolean systemProvided;

	private final Long memberId;

	private final Long subscriptionId;

	private SubscriptionPlan(
		Long id,
		String planName,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Long memberId,
		Long subscriptionId
	) {
		this.id = id;
		this.planName = planName;
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.durationMonths = durationMonths;
		this.systemProvided = systemProvided;
		this.memberId = memberId;
		this.subscriptionId = subscriptionId;
	}

	public static SubscriptionPlan withId(
		Long id,
		String planName,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Long memberId,
		Long subscriptionId
	) {
		return new SubscriptionPlan(
			id,
			planName,
			amount,
			amountUnit,
			durationMonths,
			systemProvided,
			memberId,
			subscriptionId
		);
	}

	public static SubscriptionPlan withoutId(
		String planName,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Long memberId,
		Long subscriptionId
	) {
		return new SubscriptionPlan(
			null,
			planName,
			amount,
			amountUnit,
			durationMonths,
			systemProvided,
			memberId,
			subscriptionId
		);
	}
}
