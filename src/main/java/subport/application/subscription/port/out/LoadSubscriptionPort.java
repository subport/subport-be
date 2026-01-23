package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.Subscription;

public interface LoadSubscriptionPort {

	Subscription loadSubscription(Long subscriptionId);

	List<Subscription> searchSubscriptions(Long memberId, String name);
}
