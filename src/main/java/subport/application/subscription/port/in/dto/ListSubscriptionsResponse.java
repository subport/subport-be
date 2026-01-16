package subport.application.subscription.port.in.dto;

import java.util.List;

import subport.domain.subscription.Subscription;

public record ListSubscriptionsResponse(List<SubscriptionSummary> subscriptions) {

	public static ListSubscriptionsResponse fromDomains(List<Subscription> subscriptions) {
		return new ListSubscriptionsResponse(
			subscriptions.stream()
				.map(SubscriptionSummary::fromDomain)
				.toList()
		);
	}
}
