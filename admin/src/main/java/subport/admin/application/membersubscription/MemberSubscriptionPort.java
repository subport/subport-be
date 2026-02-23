package subport.admin.application.membersubscription;

import java.time.LocalDateTime;
import java.util.List;

import subport.admin.application.dashboard.dto.DashboardTopCustomSubscriptionResponse;
import subport.admin.application.dashboard.dto.DashboardTopSubscriptionResponse;
import subport.admin.application.membersubscription.dto.MemberSubscriptionCount;

public interface MemberSubscriptionPort {

	boolean existsBySubscriptionId(Long subscriptionId);

	boolean existsByPlanId(Long planId);

	long countActiveMemberSubscriptions();

	long countActiveMemberSubscriptions(LocalDateTime start, LocalDateTime end);

	List<MemberSubscriptionCount> countActiveMemberSubscriptions(List<Long> memberIds, Boolean systemProvided);

	List<DashboardTopSubscriptionResponse> loadTopSubscriptions();

	List<DashboardTopCustomSubscriptionResponse> loadTopCustomSubscriptions();
}
