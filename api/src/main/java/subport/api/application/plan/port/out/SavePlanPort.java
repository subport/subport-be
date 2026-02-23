package subport.api.application.plan.port.out;

import subport.domain.plan.Plan;

public interface SavePlanPort {

	Long save(Plan plan);
}
