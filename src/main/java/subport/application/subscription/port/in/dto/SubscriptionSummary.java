package subport.application.subscription.port.in.dto;

import subport.domain.subscription.Subscription;

public record SubscriptionSummary(
	Long id,
	String name,
	String logoImageUrl,
	boolean defaultProvided
) {

	public static SubscriptionSummary from(Subscription subscription) {
		return new SubscriptionSummary(
			subscription.getId(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			subscription.isSystemProvided()
		);
	}
}
