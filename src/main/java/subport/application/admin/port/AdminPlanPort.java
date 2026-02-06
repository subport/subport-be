package subport.application.admin.port;

import java.util.List;

import subport.domain.subscription.Plan;

public interface AdminPlanPort {

	List<Plan> loadPlans(Long subscriptionId);
}
