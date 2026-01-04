package subport.application.subscribe.port.out;

import subport.domain.subscription.Subscription;

public interface SaveSubscriptionPort {

	void saveSubscription(Subscription subscription);
}
