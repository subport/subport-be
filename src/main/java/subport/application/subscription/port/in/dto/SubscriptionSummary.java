package subport.application.subscription.port.in.dto;

import subport.domain.subscription.Subscription;

public record SubscriptionSummary(
	Long id,
	String name
) {

	public static SubscriptionSummary fromDomain(Subscription subscription) {
		return new SubscriptionSummary(
			subscription.getId(),
			subscription.getName()
		);
	}
}
