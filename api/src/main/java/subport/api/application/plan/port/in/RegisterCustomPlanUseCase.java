package subport.api.application.plan.port.in;

import subport.api.application.plan.port.in.dto.RegisterCustomPlanRequest;
import subport.api.application.plan.port.in.dto.RegisterCustomPlanResponse;

public interface RegisterCustomPlanUseCase {

	RegisterCustomPlanResponse register(
		Long memberId,
		RegisterCustomPlanRequest request,
		Long subscriptionId
	);
}
