package subport.admin.application.port;

import java.util.List;

import subport.domain.subscription.Subscription;

public interface AdminSubscriptionPort {

	List<Subscription> loadSubscriptions();
}
