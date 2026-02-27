package subport.api.adapter.out.persistence.plan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subport.domain.plan.Plan;

public interface SpringDataPlanRepository extends JpaRepository<Plan, Long> {

	@Query("""
		SELECT p
		FROM Plan p
		WHERE p.subscription.id = :subscriptionId
		AND (p.systemProvided = true OR p.member.id = :memberId)
		""")
	List<Plan> findByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId);

	void deleteBySubscriptionId(Long subscriptionId);

	@Modifying
	@Query("""
		DELETE
		FROM Plan p
		WHERE p.member.id = :memberId
		AND p.systemProvided = false
		""")
	void deleteByMemberId(@Param("memberId") Long memberId);
}
