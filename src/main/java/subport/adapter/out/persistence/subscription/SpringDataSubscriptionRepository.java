package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataSubscriptionRepository extends JpaRepository<SubscriptionJpaEntity, Long> {

	@Query("""
		SELECT s
		FROM SubscriptionJpaEntity s
		WHERE (s.systemProvided = true OR s.member.id = :memberId)
		AND (:name IS NULL OR s.name LIKE %:name%)
		ORDER BY s.name ASC
		""")
	List<SubscriptionJpaEntity> findByMemberIdAndNameContaining(
		@Param("memberId") Long memberId,
		@Param("name") String name
	);
}
