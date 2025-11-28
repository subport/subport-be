package subscribe.adapter.out.persistence.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSubscriptionPlanRepository extends JpaRepository<SubscriptionPlanJpaEntity, Long> {
}
