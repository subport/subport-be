package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataPlanRepository extends JpaRepository<PlanJpaEntity, Long> {

	@Query("""
		SELECT p
		FROM PlanJpaEntity p
		WHERE p.subscription.id = :subscriptionId
		AND (p.systemProvided = true OR p.member.id = :memberId)
		""")
	List<PlanJpaEntity> findByIdAccessibleToMember(Long memberId, Long subscriptionId);

	void deleteAllBySubscriptionId(Long subscriptionId);
}
