package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.Plan;

public interface LoadPlanPort {

	Plan loadPlan(Long planId);

	List<Plan> loadPlans(Long memberId, Long subscriptionId);
}
