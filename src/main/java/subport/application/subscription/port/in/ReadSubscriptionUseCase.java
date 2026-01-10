package subport.application.subscription.port.in;

import subport.application.subscription.port.out.ListSubscriptionsResponse;

public interface ReadSubscriptionUseCase {

	ListSubscriptionsResponse list(Long memberId);
}
