package subport.application.subscription.port.out;

import java.util.List;

import subport.domain.subscription.SubscriptionPlan;

public interface LoadSubscriptionPlanPort {

	List<SubscriptionPlan> loadByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId);
}
