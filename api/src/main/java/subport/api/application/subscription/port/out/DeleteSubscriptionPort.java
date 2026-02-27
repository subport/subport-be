package subport.api.application.subscription.port.out;

import subport.domain.subscription.Subscription;

public interface DeleteSubscriptionPort {

	void delete(Subscription subscription);

	void deleteByMemberId(Long memberId);
}
