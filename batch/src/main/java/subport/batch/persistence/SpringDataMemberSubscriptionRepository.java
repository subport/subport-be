package subport.batch.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subport.domain.membersubscription.MemberSubscription;

public interface SpringDataMemberSubscriptionRepository extends JpaRepository<MemberSubscription, Long> {

	@EntityGraph(attributePaths = {
		"member",
		"subscription",
		"plan"
	})
	@Query("""
		SELECT ms
		FROM MemberSubscription ms
		JOIN FETCH ms.member m
		JOIN FETCH ms.subscription s
		JOIN FETCH ms.plan p
		WHERE ms.nextPaymentDate = :nextPaymentDate
		AND ms.active = true
		""")
	List<MemberSubscription> findActiveByNextPaymentDate(@Param("nextPaymentDate") LocalDate nextPaymentDate);

	@Query("""
		SELECT ms
		FROM MemberSubscription ms
		JOIN FETCH ms.member m
		JOIN FETCH ms.subscription s
		WHERE ms.paymentReminderDate = :reminderDate
		AND ms.active = true
		""")
	List<MemberSubscription> findActiveByPaymentReminderDate(@Param("reminderDate") LocalDate reminderDate);
}
