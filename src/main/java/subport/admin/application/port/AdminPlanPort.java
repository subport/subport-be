package subport.admin.application.port;

import java.util.List;

import subport.domain.subscription.Plan;

public interface AdminPlanPort {

	Long save(Plan plan);

	List<Plan> loadPlans(Long subscriptionId);

	Plan loadPlan(Long planId);

	void delete(Plan plan);
}
