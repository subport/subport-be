package subport.domain.subscription;

import lombok.Getter;

@Getter
public class SubscriptionType {

	private final Long id;

	private final String name;

	private final boolean isDefault;

	private final Long memberId;

	private SubscriptionType(
		Long id,
		String name,
		Long memberId
	) {
		this.id = id;
		this.name = name;
		this.isDefault = false;
		this.memberId = memberId;
	}

	public static SubscriptionType withId(
		Long id,
		String name,
		Long memberId
	) {
		return new SubscriptionType(
			id,
			name,
			memberId
		);
	}

	public static SubscriptionType withoutId(
		String name,
		Long memberId
	) {
		return new SubscriptionType(
			null,
			name,
			memberId
		);
	}
}
