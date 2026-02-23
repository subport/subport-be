package subport.admin.application.plan;

import java.util.List;

import subport.domain.plan.Plan;

public interface PlanPort {

	Long save(Plan plan);

	List<Plan> loadPlans(Long subscriptionId);

	Plan loadPlan(Long planId);

	void delete(Plan plan);
}
