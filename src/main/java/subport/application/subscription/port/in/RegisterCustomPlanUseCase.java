package subport.application.subscription.port.in;

import subport.application.subscription.port.out.RegisterCustomPlanResponse;

public interface RegisterCustomPlanUseCase {

	RegisterCustomPlanResponse register(
		Long memberId,
		RegisterCustomPlanRequest request,
		Long subscriptionId
	);
}
