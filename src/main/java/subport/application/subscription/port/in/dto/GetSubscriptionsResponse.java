package subport.application.subscription.port.in.dto;

import java.util.List;

import subport.domain.subscription.Subscription;

public record GetSubscriptionsResponse(List<SubscriptionSummary> subscriptions) {

	public static GetSubscriptionsResponse from(List<Subscription> subscriptions) {
		return new GetSubscriptionsResponse(
			subscriptions.stream()
				.map(SubscriptionSummary::from)
				.toList()
		);
	}
}
