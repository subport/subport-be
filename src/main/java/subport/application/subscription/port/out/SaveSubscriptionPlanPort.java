package subport.application.subscription.port.out;

import subport.domain.subscription.SubscriptionPlan;

public interface SaveSubscriptionPlanPort {

	void save(SubscriptionPlan subscriptionPlan);
}
