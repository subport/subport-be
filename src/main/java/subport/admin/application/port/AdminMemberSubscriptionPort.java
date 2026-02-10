package subport.admin.application.port;

public interface AdminMemberSubscriptionPort {

	boolean existsBySubscriptionId(Long subscriptionId);

	boolean existsByPlanId(Long planId);
}
