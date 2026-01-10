package subport.application.subscription.port.in;

import subport.application.subscription.port.out.ListSubscriptionsResponse;
import subport.application.subscription.port.out.ReadSubscriptionResponse;

public interface ReadSubscriptionUseCase {

	ReadSubscriptionResponse read(Long memberId, Long subscriptionId);

	ListSubscriptionsResponse list(Long memberId);
}
