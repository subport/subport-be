package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

	List<SubscriptionJpaEntity> findByMemberIdOrSystemProvidedOrderByNameAsc(Long memberId, boolean systemProvided);
}
