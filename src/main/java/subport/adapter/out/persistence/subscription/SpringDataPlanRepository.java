package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.subscription.Plan;

public interface SpringDataPlanRepository extends JpaRepository<Plan, Long> {

	@Query("""
		SELECT p
		FROM Plan p
		WHERE p.subscription.id = :subscriptionId
		AND (p.systemProvided = true OR p.member.id = :memberId)
		""")
	List<Plan> findByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId);

	void deleteBySubscriptionId(Long subscriptionId);
}
