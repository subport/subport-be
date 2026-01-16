package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.UpdateCustomPlanRequest;

public interface UpdateCustomPlanUseCase {

	void update(
		Long memberId,
		UpdateCustomPlanRequest request,
		Long planId
	);
}
