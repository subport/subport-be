package subport.adapter.out.persistence.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSubscriptionPlanRepository extends JpaRepository<SubscriptionPlanJpaEntity, Long> {

	void deleteAllBySubscriptionId(Long subscriptionId);
}
