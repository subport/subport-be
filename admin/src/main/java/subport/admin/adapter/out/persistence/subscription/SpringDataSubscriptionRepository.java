package subport.admin.adapter.out.persistence.subscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

public interface SpringDataSubscriptionRepository extends JpaRepository<Subscription, Long> {

	@Query("""
		SELECT s
		FROM Subscription s
		WHERE s.systemProvided = true
		AND (:type IS NULL OR s.type = :type)
		AND (:name IS NULL OR s.name LIKE %:name%)
		""")
	Page<Subscription> findByTypeContainingAndNameContaining(
		SubscriptionType type,
		String name,
		Pageable pageable
	);
}
