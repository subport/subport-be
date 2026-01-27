package subport.application.subscription.port.out;

import subport.domain.subscription.Subscription;

public interface DeleteSubscriptionPort {

	void delete(Subscription subscription);
}
