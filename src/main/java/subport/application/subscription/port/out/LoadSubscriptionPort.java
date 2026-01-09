package subport.application.subscription.port.out;

import subport.domain.subscription.Subscription;

public interface LoadSubscriptionPort {

	Subscription load(Long id);
}
