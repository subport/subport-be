package subport.admin.adapter.out.persistence.membersubscription;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.admin.application.dashboard.dto.DashboardTopCustomSubscriptionResponse;
import subport.admin.application.dashboard.dto.DashboardTopSubscriptionResponse;
import subport.admin.application.membersubscription.dto.MemberSubscriptionCount;
import subport.domain.membersubscription.MemberSubscription;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscription, Long> {

	boolean existsBySubscriptionId(Long subscriptionId);

	boolean existsByPlanId(Long planId);

	@Query("""
		SELECT count(ms)
		FROM MemberSubscription ms
		WHERE ms.active = true
		AND ms.member.role = subport.domain.member.MemberRole.MEMBER
		""")
	long countActiveMemberSubscriptions();

	@Query("""
		SELECT count(ms)
		FROM MemberSubscription ms
		WHERE (ms.createdAt >= :start AND ms.createdAt < :end)
		AND ms.active = true
		AND ms.member.role = subport.domain.member.MemberRole.MEMBER
		""")
	long countActiveMemberSubscriptions(LocalDateTime start, LocalDateTime end);

	@Query("""
		SELECT new subport.admin.application.membersubscription.dto.MemberSubscriptionCount(
		    ms.member.id,
		    count(ms)
		)
		FROM MemberSubscription ms
		WHERE ms.member.id in :memberIds
		AND (:systemProvided IS NULL OR ms.subscription.systemProvided = :systemProvided)
		AND ms.active = true
		GROUP BY ms.member.id
		""")
	List<MemberSubscriptionCount> countActiveMemberSubscriptionsByMember(List<Long> memberIds, Boolean systemProvided);

	@Query("""
		SELECT new subport.admin.application.dashboard.dto.DashboardTopSubscriptionResponse(
			ms.subscription.name,
			ms.subscription.logoImageUrl,
			COUNT(DISTINCT ms.member.id)
		)
		FROM MemberSubscription ms
		WHERE ms.active = true
		AND ms.subscription.systemProvided = true
		AND ms.member.role = subport.domain.member.MemberRole.MEMBER
		GROUP BY ms.subscription.name, ms.subscription.logoImageUrl
		ORDER BY COUNT(ms) DESC, ms.subscription.name ASC
		""")
	List<DashboardTopSubscriptionResponse> findTopSubscriptions(Pageable pageable);

	@Query("""
		SELECT new subport.admin.application.dashboard.dto.DashboardTopCustomSubscriptionResponse(
		    ms.subscription.normalizedName,
		    COUNT(DISTINCT ms.member.id)
		)
		FROM MemberSubscription ms
		WHERE ms.active = true
		AND ms.subscription.systemProvided = false
		AND ms.member.role = subport.domain.member.MemberRole.MEMBER
		GROUP BY ms.subscription.normalizedName
		HAVING COUNT(DISTINCT ms.member.id) >= 2
		ORDER BY COUNT(DISTINCT ms.member.id) DESC, ms.subscription.normalizedName ASC
		""")
	List<DashboardTopCustomSubscriptionResponse> findTopCustomSubscriptions(Pageable pageable);
}
