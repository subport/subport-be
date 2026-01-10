package subport.application.subscription.port.out;

public interface DeletePlanPort {

	void deleteById(Long planId);

	void deleteBySubscriptionId(Long subscriptionId);
}
