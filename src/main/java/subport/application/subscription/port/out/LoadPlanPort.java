package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.Plan;

public interface LoadPlanPort {

	Plan load(Long subscriptionPlanId);

	List<Plan> loadByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId);
}
