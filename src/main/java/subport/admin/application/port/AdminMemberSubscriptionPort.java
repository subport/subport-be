package subport.admin.application.port;

import java.time.LocalDateTime;

public interface AdminMemberSubscriptionPort {

	boolean existsBySubscriptionId(Long subscriptionId);

	boolean existsByPlanId(Long planId);

	long countActiveMemberSubscriptions();

	long countActiveMemberSubscriptions(LocalDateTime start, LocalDateTime end);
}
