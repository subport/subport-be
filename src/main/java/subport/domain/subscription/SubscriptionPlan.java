package subport.domain.subscription;

import lombok.Getter;

@Getter
public class SubscriptionPlan {

	private final Long id;

	private final String planName;

	private final int amount;

	private final SubscriptionAmountUnit amountUnit;

	private final int durationMonths;

	private final boolean systemProvided;

	private final Long memberId;

	private final Long subscriptionId;

	private SubscriptionPlan(
		Long id,
		String planName,
		int amount,
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
		int amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		Long memberId,
		Long subscriptionId
	) {
		return new SubscriptionPlan(
			id,
			planName,
			amount,
			amountUnit,
			durationMonths,
			false,
			memberId,
			subscriptionId
		);
	}

	public static SubscriptionPlan withoutId(
		String planName,
		int amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		Long memberId,
		Long subscriptionId
	) {
		return new SubscriptionPlan(
			null,
			planName,
			amount,
			amountUnit,
			durationMonths,
			false,
			memberId,
			subscriptionId
		);
	}
}
