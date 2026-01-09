package subport.domain.subscription;

import lombok.Getter;

@Getter
public class Subscription {

	private final Long id;

	private String name;

	private SubscriptionType type;

	private String logoImageUrl;

	private String planUrl;

	private final boolean systemProvided;

	private final Long memberId;

	private Subscription(
		Long id,
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		boolean systemProvided,
		Long memberId
	) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.logoImageUrl = logoImageUrl;
		this.planUrl = planUrl;
		this.systemProvided = systemProvided;
		this.memberId = memberId;
	}

	public static Subscription withId(
		Long id,
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		boolean systemProvided,
		Long memberId
	) {
		return new Subscription(
			id,
			name,
			type,
			logoImageUrl,
			planUrl,
			systemProvided,
			memberId
		);
	}

	public static Subscription withoutId(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl,
		boolean systemProvided,
		Long memberId
	) {
		return new Subscription(
			null,
			name,
			type,
			logoImageUrl,
			planUrl,
			systemProvided,
			memberId
		);
	}

	public void update(
		String name,
		SubscriptionType type,
		String logoImageUrl,
		String planUrl
	) {
		this.name = name;
		this.type = type;
		if (logoImageUrl != null) {
			this.logoImageUrl = logoImageUrl;
		}
		this.planUrl = planUrl;
	}
}