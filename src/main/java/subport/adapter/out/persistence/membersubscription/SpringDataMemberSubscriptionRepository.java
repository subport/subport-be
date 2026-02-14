package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.admin.application.dto.DashboardTopServiceResponse;
import subport.admin.application.query.CustomMemberSubscriptionCount;
import subport.admin.application.query.MemberSubscriptionCount;
import subport.domain.membersubscription.MemberSubscription;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscription, Long> {

	@Query("""
		SELECT ms
		FROM MemberSubscription ms
		JOIN FETCH ms.member
		JOIN FETCH ms.subscription
		JOIN FETCH ms.plan
		WHERE ms.id = :id
		""")
	Optional<MemberSubscription> findByIdWithFetch(Long id);

	List<MemberSubscription> findByMemberIdAndActiveIsTrue(Long memberId);

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	List<MemberSubscription> findByMemberIdAndActive(
		Long memberId,
		boolean active,
		Sort sort
	);

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	List<MemberSubscription> findByNextPaymentDateAndActiveTrue(LocalDate nextPaymentDate);

	@EntityGraph(attributePaths = {
		"member",
		"subscription"
	})
	List<MemberSubscription> findByPaymentReminderDateAndActiveTrue(LocalDate reminderDate);

	List<MemberSubscription> findByMemberIdAndLastPaymentDateGreaterThanEqualAndLastPaymentDateLessThan(
		Long memberId,
		LocalDate lastPaymentDateIsGreaterThan,
		LocalDate lastPaymentDateIsLessThan
	);

	List<MemberSubscription> findByMemberIdAndLastPaymentDateAndActiveTrue(Long memberId, LocalDate lastPaymentDate);

	boolean existsBySubscriptionId(Long subscriptionId);

	boolean existsByPlanId(Long planId);

	@Query("""
		SELECT count(ms)
		FROM MemberSubscription ms
		WHERE ms.active = true
		""")
	long countActiveMemberSubscriptions();

	@Query("""
		SELECT count(ms)
		FROM MemberSubscription ms
		WHERE (ms.createdAt >= :start AND ms.createdAt < :end)
		AND ms.active = true
		""")
	long countActiveMemberSubscriptions(LocalDateTime start, LocalDateTime end);

	@Query("""
		SELECT new subport.admin.application.query.MemberSubscriptionCount(
		    ms.member.id,
		    count(ms)
		)
		FROM MemberSubscription ms
		WHERE ms.member.id in :memberIds
		AND ms.active = true
		GROUP BY ms.member.id
		""")
	List<MemberSubscriptionCount> countActiveMemberSubscriptionsByMember(List<Long> memberIds);

	@Query("""
		SELECT new subport.admin.application.dto.DashboardTopServiceResponse(
			ms.subscription.name,
			COUNT(ms)
		)
		FROM MemberSubscription ms
		WHERE ms.active = true
		GROUP BY ms.subscription.name
		ORDER BY COUNT(ms) DESC, ms.subscription.name ASC
		""")
	List<DashboardTopServiceResponse> countActiveMemberSubscriptionsBySubscription(Pageable top5);

	@Query("""
		SELECT new subport.admin.application.query.CustomMemberSubscriptionCount(
		    ms.subscription.normalizedName,
		    ms.subscription.name,
		    COUNT(ms)
		)
		FROM MemberSubscription ms
		WHERE ms.active = true
		AND ms.subscription.systemProvided = false
		GROUP BY ms.subscription.normalizedName, ms.subscription.name
		""")
	List<CustomMemberSubscriptionCount> countActiveCustomMemberSubscriptions();
}
