package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.ListSubscriptionsResponse;
import subport.application.subscription.port.in.dto.ReadSubscriptionResponse;

public interface SubscriptionQueryUseCase {

	ReadSubscriptionResponse getSubscription(Long memberId, Long subscriptionId);

	ListSubscriptionsResponse searchSubscriptions(Long memberId, String name);
}
