package subport.batch.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

public interface SpringDataEmailNotificationRepository extends JpaRepository<EmailNotification, Long> {

	@Query("""
		SELECT en
		FROM EmailNotification en
		WHERE en.createdAt >= :start
		AND en.createdAt < :end
		AND en.status = :status
		""")
	List<EmailNotification> findByStatusAndCreatedAtBetween(
		@Param("start") LocalDateTime start,
		@Param("end") LocalDateTime end,
		@Param("status") SendingStatus status
	);
}
