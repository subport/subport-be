package subport.api.application.plan.port.out;

import subport.domain.plan.Plan;

public interface DeletePlanPort {

	void delete(Plan plan);

	void deleteBySubscriptionId(Long subscriptionId);

	void deleteByMemberId(Long memberId);
}
