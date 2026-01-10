package subport.application.subscription.port.out;

import subport.domain.subscription.SubscriptionPlan;

public interface UpdateSubscriptionPlanPort {

	void update(SubscriptionPlan subscriptionPlan);
}
