package subport.batch.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import subport.domain.subscription.Subscription;

public interface SpringDataSubscriptionRepository extends JpaRepository<Subscription, Long> {

	@Modifying
	@Query("""
		DELETE FROM Subscription s
		WHERE s.member.id IN :memberIds
		""")
	void deleteAllByMemberIds(List<Long> memberIds);
}
