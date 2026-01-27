package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.application.subscription.port.in.dto.GetSubscriptionsResponse;

public interface SubscriptionQueryUseCase {

	GetSubscriptionResponse getSubscription(Long memberId, Long subscriptionId);

	GetSubscriptionsResponse searchSubscriptions(Long memberId, String name);
}
