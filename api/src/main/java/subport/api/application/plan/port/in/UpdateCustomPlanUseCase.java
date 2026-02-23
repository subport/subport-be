package subport.api.application.plan.port.in;

import subport.api.application.plan.port.in.dto.GetPlanResponse;
import subport.api.application.plan.port.in.dto.UpdateCustomPlanRequest;

public interface UpdateCustomPlanUseCase {

	GetPlanResponse update(
		Long memberId,
		UpdateCustomPlanRequest request,
		Long planId
	);
}
