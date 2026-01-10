package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.Subscription;

public interface LoadSubscriptionPort {

	Subscription load(Long subscriptionId);

	List<Subscription> loadByMemberId(Long memberId);
}
