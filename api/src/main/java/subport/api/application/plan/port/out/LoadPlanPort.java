package subport.api.application.plan.port.out;

import java.util.List;

import subport.domain.plan.Plan;

public interface LoadPlanPort {

	Plan loadPlan(Long planId);

	List<Plan> loadPlans(Long memberId, Long subscriptionId);
}
