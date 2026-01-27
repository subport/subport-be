package subport.application.subscription.port.out;

import subport.domain.subscription.Plan;

public interface DeletePlanPort {

	void delete(Plan plan);

	void delete(Long subscriptionId);
}
