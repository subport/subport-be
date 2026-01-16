package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.ListSubscriptionsResponse;
import subport.application.subscription.port.in.dto.ReadSubscriptionResponse;

public interface ReadSubscriptionUseCase {

	ReadSubscriptionResponse read(Long memberId, Long subscriptionId);

	ListSubscriptionsResponse list(Long memberId);
}
