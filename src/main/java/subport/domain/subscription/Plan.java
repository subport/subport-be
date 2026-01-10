package subport.domain.subscription;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class Plan {

	private final Long id;

	private String name;

	private BigDecimal amount;

	private SubscriptionAmountUnit amountUnit;

	private int durationMonths;

	private final boolean systemProvided;

	private final Long memberId;

	private final Long subscriptionId;

	private Plan(
		Long id,
		String name,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Long memberId,
		Long subscriptionId
	) {
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.durationMonths = durationMonths;
		this.systemProvided = systemProvided;
		this.memberId = memberId;
		this.subscriptionId = subscriptionId;
	}

	public static Plan withId(
		Long id,
		String name,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Long memberId,
		Long subscriptionId
	) {
		return new Plan(
			id,
			name,
			amount,
			amountUnit,
			durationMonths,
			systemProvided,
			memberId,
			subscriptionId
		);
	}

	public static Plan withoutId(
		String name,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths,
		boolean systemProvided,
		Long memberId,
		Long subscriptionId
	) {
		return new Plan(
			null,
			name,
			amount,
			amountUnit,
			durationMonths,
			systemProvided,
			memberId,
			subscriptionId
		);
	}

	public void update(
		String name,
		BigDecimal amount,
		SubscriptionAmountUnit amountUnit,
		int durationMonths
	) {
		this.name = name;
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.durationMonths = durationMonths;
	}
}
