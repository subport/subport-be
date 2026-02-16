package subport.adapter.out.persistence.emailnotification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.admin.application.query.EmailStatusCount;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

public interface SpringDataEmailNotificationRepository extends JpaRepository<EmailNotification, Long> {

	List<EmailNotification> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndStatus(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status
	);

	@Query("""
		SELECT e
		FROM EmailNotification e
		WHERE e.createdAt >= :start AND e.createdAt < :end
		""")
	List<EmailNotification> findByCreatedAt(
		LocalDateTime start,
		LocalDateTime end
	);

	@Query("""
		SELECT new subport.admin.application.query.EmailStatusCount(
		    e.status,
		    COUNT(e)
		)
		FROM EmailNotification e
		WHERE e.createdAt >= :start AND e.createdAt < :end
		GROUP BY e.status
		""")
	List<EmailStatusCount> countTodayByStatus(
		LocalDateTime start,
		LocalDateTime end
	);
}
