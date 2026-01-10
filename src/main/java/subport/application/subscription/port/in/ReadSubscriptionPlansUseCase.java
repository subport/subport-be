package subport.application.subscription.port.in;

import subport.application.subscription.port.out.ListSubscriptionPlansResponse;

public interface ReadSubscriptionPlansUseCase {

	ListSubscriptionPlansResponse list(Long memberId, Long subscriptionId);
}
