package subport.application.subscribe.port.out;

import subport.domain.subscription.Subscription;

public interface SaveCustomSubscriptionPort {

	void save(Subscription subscription);
}
