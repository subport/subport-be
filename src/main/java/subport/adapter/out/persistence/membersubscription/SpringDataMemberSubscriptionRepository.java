package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscriptionJpaEntity, Long> {

	@Query("""
		SELECT ms
		FROM MemberSubscriptionJpaEntity ms
		JOIN FETCH ms.member
		JOIN FETCH ms.subscription
		JOIN FETCH ms.plan
		WHERE ms.nextPaymentDate = :nextPaymentDate
		AND ms.active = true
		""")
	List<MemberSubscriptionJpaEntity> findByNextPaymentDateAndActiveTrueWithFetch(LocalDate nextPaymentDate);

	@Query("""
		SELECT ms
		FROM MemberSubscriptionJpaEntity ms
		JOIN FETCH ms.member
		JOIN FETCH ms.subscription
		JOIN FETCH ms.plan
		WHERE ms.id = :id
		""")
	Optional<MemberSubscriptionJpaEntity> findByIdWithFetch(Long id);

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	List<MemberSubscriptionJpaEntity> findByMemberIdAndActive(
		Long memberId,
		boolean active,
		Sort sort
	);
}
