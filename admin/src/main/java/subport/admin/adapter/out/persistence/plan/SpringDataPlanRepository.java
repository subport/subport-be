package subport.admin.adapter.out.persistence.plan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.plan.Plan;

public interface SpringDataPlanRepository extends JpaRepository<Plan, Long> {

	List<Plan> findBySubscriptionId(Long subscriptionId);
}
