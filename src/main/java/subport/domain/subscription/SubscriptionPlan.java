package subport.domain.subscription;

import lombok.Getter;

@Getter
public class SubscriptionPlan {

	private final Long id;

	private final String subscriptionName;

	private final String planName;

	private final int amount;

	private final int durationMonths;

	private final boolean isDefault;

	private final Long memberId;

	private SubscriptionPlan(
		Long id,
		String subscriptionName,
		String planName,
		int amount,
		int durationMonths,
		Long memberId
	) {
		this.id = id;
		this.subscriptionName = subscriptionName;
		this.planName = planName;
		this.amount = amount;
		this.durationMonths = durationMonths;
		this.isDefault = false;
		this.memberId = memberId;
	}

	public static SubscriptionPlan withId(
		Long id,
		String subscriptionName,
		String planName,
		int amount,
		int durationMonths,
		Long memberId
	) {
		return new SubscriptionPlan(
			id,
			subscriptionName,
			planName,
			amount,
			durationMonths,
			memberId
		);
	}

	public static SubscriptionPlan withoutId(
		String subscriptionName,
		String planName,
		int amount,
		int durationMonths,
		Long memberId
	) {
		return new SubscriptionPlan(
			null,
			subscriptionName,
			planName,
			amount,
			durationMonths,
			memberId
		);
	}
}
