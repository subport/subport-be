package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subport.domain.subscription.Subscription;

public interface SpringDataSubscriptionRepository extends JpaRepository<Subscription, Long> {

	@Query("""
		SELECT s
		FROM Subscription s
		WHERE (s.systemProvided = true OR s.member.id = :memberId)
		AND (:name IS NULL OR s.name LIKE %:name%)
		ORDER BY s.name ASC
		""")
	List<Subscription> findByMemberIdAndNameContaining(
		@Param("memberId") Long memberId,
		@Param("name") String name
	);
}
