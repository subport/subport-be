package subport.api.application.plan.port.out;

import subport.domain.plan.Plan;

public interface DeletePlanPort {

	void delete(Plan plan);

	void delete(Long subscriptionId);
}
