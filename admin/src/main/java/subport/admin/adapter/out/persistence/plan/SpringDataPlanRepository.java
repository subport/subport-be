package subport.admin.adapter.out.persistence.plan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.plan.Plan;

public interface SpringDataPlanRepository extends JpaRepository<Plan, Long> {

	@Query("""
		SELECT p
		FROM Plan p
		WHERE p.subscription.id = :subscriptionId
		AND p.systemProvided = true
		""")
	List<Plan> findBySubscriptionId(Long subscriptionId);
}
