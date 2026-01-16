package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.RegisterCustomPlanRequest;
import subport.application.subscription.port.in.dto.RegisterCustomPlanResponse;

public interface RegisterCustomPlanUseCase {

	RegisterCustomPlanResponse register(
		Long memberId,
		RegisterCustomPlanRequest request,
		Long subscriptionId
	);
}
