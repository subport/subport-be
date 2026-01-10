package subport.application.subscription.port.in;

import subport.application.subscription.port.out.ListSubscriptionPlansResponse;
import subport.application.subscription.port.out.ReadSubscriptionPlanResponse;

public interface ReadSubscriptionPlansUseCase {

	ReadSubscriptionPlanResponse read(Long memberId, Long planId);

	ListSubscriptionPlansResponse list(Long memberId, Long subscriptionId);
}
