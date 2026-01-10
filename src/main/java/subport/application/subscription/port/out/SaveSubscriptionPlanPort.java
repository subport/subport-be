package subport.application.subscription.port.out;

import subport.domain.subscription.SubscriptionPlan;

public interface SaveSubscriptionPlanPort {

	Long save(SubscriptionPlan subscriptionPlan);
}
