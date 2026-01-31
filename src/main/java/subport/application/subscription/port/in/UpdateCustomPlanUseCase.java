package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.GetPlanResponse;
import subport.application.subscription.port.in.dto.UpdateCustomPlanRequest;

public interface UpdateCustomPlanUseCase {

	GetPlanResponse update(
		Long memberId,
		UpdateCustomPlanRequest request,
		Long planId
	);
}
