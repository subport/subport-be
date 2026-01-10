package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataSubscriptionPlanRepository extends JpaRepository<SubscriptionPlanJpaEntity, Long> {

	@Query("""
		SELECT s
		FROM SubscriptionPlanJpaEntity s
		WHERE s.subscription.id = :subscriptionId
		AND (s.systemProvided = true OR s.member.id = :memberId)
		""")
	List<SubscriptionPlanJpaEntity> findByIdAccessibleToMember(Long memberId, Long subscriptionId);

	void deleteAllBySubscriptionId(Long subscriptionId);
}
