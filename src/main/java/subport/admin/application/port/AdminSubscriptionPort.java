package subport.admin.application.port;

import java.util.List;

import subport.domain.subscription.Subscription;

public interface AdminSubscriptionPort {

	Long save(Subscription subscription);

	Subscription loadSubscription(Long subscriptionId);

	List<Subscription> loadSubscriptions();

	void delete(Subscription subscription);
}
