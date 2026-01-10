package subport.application.subscription.port.out;

import subport.domain.subscription.Plan;

public interface SavePlanPort {

	Long save(Plan plan);
}
