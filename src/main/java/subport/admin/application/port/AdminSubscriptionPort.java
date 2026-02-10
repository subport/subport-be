package subport.admin.application.port;

import java.util.List;

import org.springframework.data.domain.Pageable;

import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

public interface AdminSubscriptionPort {

	Long save(Subscription subscription);

	Subscription loadSubscription(Long subscriptionId);

	List<Subscription> searchSubscriptions(
		SubscriptionType type,
		String name,
		Pageable pageable
	);

	void delete(Subscription subscription);
}
