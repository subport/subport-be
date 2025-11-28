package subscribe.application.subscribe.port.out;

import subscribe.domain.subscription.Subscription;

public interface SaveSubscriptionPort {

	void saveSubscription(Subscription subscription);
}
