package subport.admin.application.port;

import java.time.LocalDateTime;
import java.util.List;

import subport.admin.application.query.MemberSubscriptionCount;

public interface AdminMemberSubscriptionPort {

	boolean existsBySubscriptionId(Long subscriptionId);

	boolean existsByPlanId(Long planId);

	long countActiveMemberSubscriptions();

	long countActiveMemberSubscriptions(LocalDateTime start, LocalDateTime end);

	List<MemberSubscriptionCount> countActiveMemberSubscriptions(List<Long> memberIds);
}
