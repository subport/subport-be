package subport.application.subscription.port.in.dto;

import subport.domain.subscription.Subscription;

public record GetSubscriptionResponse(
	Long id,
	String name,
	String type,
	String logoImageUrl
) {

	public static GetSubscriptionResponse from(Subscription subscription) {
		return new GetSubscriptionResponse(
			subscription.getId(),
			subscription.getName(),
			subscription.getType().getDisplayName(),
			subscription.getLogoImageUrl()
		);
	}
}
