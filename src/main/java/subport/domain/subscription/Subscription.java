package subport.domain.subscription;

import lombok.Getter;

@Getter
public class Subscription {

	private final Long id;

	private final String name;

	private final SubscriptionType type;

	private final String logoImageUrl;

	private final String planUrl;

	private final boolean systemProvided;

	private final Long memberId;

	private Subscription(
		Long id,
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		Long memberId
	) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.logoImageUrl = logoImageUrl;
		this.planUrl = planUrl;
		this.systemProvided = false;
		this.memberId = memberId;
	}

	public static Subscription withId(
		Long id,
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		Long memberId
	) {
		return new Subscription(
			id,
			name,
			type,
			logoImageUrl,
			planUrl,
			memberId
		);
	}

	public static Subscription withoutId(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		Long memberId
	) {
		return new Subscription(
			null,
			name,
			type,
			logoImageUrl,
			planUrl,
			memberId
		);
	}
}