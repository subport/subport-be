package subport.application.subscription.port.out;

public interface DeleteSubscriptionPlanPort {

	void deleteById(Long planId);

	void deleteBySubscriptionId(Long subscriptionId);
}
