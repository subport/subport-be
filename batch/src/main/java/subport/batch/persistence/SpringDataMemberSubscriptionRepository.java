package subport.batch.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
		WHERE ms.active = true
		AND ms.member.paymentReminderEnabled = true
		AND ms.paymentReminderDate = :reminderDate
		""")
	List<MemberSubscription> findActiveByPaymentReminderDate(@Param("reminderDate") LocalDate reminderDate);

	@Modifying
	@Query("""
		DELETE FROM MemberSubscription ms
		WHERE ms.member.id IN :memberIds
		""")
	void deleteAllByMemberIds(List<Long> memberIds);
}
