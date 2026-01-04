package subport.adapter.out.persistence.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSubscriptionTypeRepository extends JpaRepository<SubscriptionTypeJpaEntity, Long> {
}
