package subport.application.subscription.port.in;

import subport.application.subscription.port.in.dto.GetPlanResponse;
import subport.application.subscription.port.in.dto.GetPlansResponse;

public interface PlanQueryUseCase {

	GetPlanResponse getPlan(Long memberId, Long planId);

	GetPlansResponse getPlans(Long memberId, Long subscriptionId);
}
