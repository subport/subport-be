package subport.api.application.plan.port.in;

import subport.api.application.plan.port.in.dto.GetPlanResponse;
import subport.api.application.plan.port.in.dto.GetPlansResponse;

public interface PlanQueryUseCase {

	GetPlanResponse getPlan(Long memberId, Long planId);

	GetPlansResponse getPlans(Long memberId, Long subscriptionId);
}
