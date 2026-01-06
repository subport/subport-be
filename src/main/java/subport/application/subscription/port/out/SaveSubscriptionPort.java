package subport.application.subscription.port.out;

import subport.domain.subscription.Subscription;

public interface SaveSubscriptionPort {

	void save(Subscription subscription);
}
