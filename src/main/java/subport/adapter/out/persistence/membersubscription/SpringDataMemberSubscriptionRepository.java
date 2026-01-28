package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.membersubscription.MemberSubscription;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscription, Long> {

	@Query("""
		SELECT ms
		FROM MemberSubscription ms
		JOIN FETCH ms.member
		JOIN FETCH ms.subscription
		JOIN FETCH ms.plan
		WHERE ms.id = :id
		""")
	Optional<MemberSubscription> findByIdWithFetch(Long id);

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	List<MemberSubscription> findByMemberIdAndActive(
		Long memberId,
		boolean active,
		Sort sort
	);

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	List<MemberSubscription> findByNextPaymentDateAndActiveTrue(LocalDate nextPaymentDate);

	@EntityGraph(attributePaths = {
		"member",
		"subscription"
	})
	List<MemberSubscription> findByReminderDateAndActiveTrue(LocalDate reminderDate);
}
