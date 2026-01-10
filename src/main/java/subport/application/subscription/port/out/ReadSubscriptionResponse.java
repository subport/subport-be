package subport.application.subscription.port.out;

import subport.domain.subscription.Subscription;

public record ReadSubscriptionResponse(
	Long id,
	String name,
	String type
) {

	public static ReadSubscriptionResponse fromDomain(Subscription subscription) {
		return new ReadSubscriptionResponse(
			subscription.getId(),
			subscription.getName(),
			subscription.getType().getDisplayName()
		);
	}
}
