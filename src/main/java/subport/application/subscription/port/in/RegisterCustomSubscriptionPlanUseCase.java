package subport.application.subscription.port.in;

import subport.application.subscription.port.out.RegisterCustomSubscriptionPlanResponse;

public interface RegisterCustomSubscriptionPlanUseCase {

	RegisterCustomSubscriptionPlanResponse register(
		Long memberId,
		RegisterCustomSubscriptionPlanRequest request,
		Long subscriptionId
	);
}
