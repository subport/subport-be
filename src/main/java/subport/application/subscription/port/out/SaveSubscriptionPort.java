package subport.application.subscription.port.out;

import subport.domain.subscription.Subscription;

public interface SaveSubscriptionPort {

	Long save(Subscription subscription);
}
