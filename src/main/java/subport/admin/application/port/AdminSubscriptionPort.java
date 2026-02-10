package subport.admin.application.port;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

public interface AdminSubscriptionPort {

	Long save(Subscription subscription);

	Subscription loadSubscription(Long subscriptionId);

	Page<Subscription> searchSubscriptions(
		SubscriptionType type,
		String name,
		Pageable pageable
	);

	void delete(Subscription subscription);
}
