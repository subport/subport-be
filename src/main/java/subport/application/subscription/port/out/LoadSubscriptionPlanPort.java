package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.SubscriptionPlan;

public interface LoadSubscriptionPlanPort {

	SubscriptionPlan load(Long subscriptionPlanId);

	List<SubscriptionPlan> loadByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId);
}
