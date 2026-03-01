package subport.admin.adapter.out.persistence.emailnotification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

public interface SpringDataEmailNotificationRepository extends JpaRepository<EmailNotification, Long> {

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
		SELECT distinct e.recipientEmail
		FROM EmailNotification e
		WHERE (:start IS NULL OR e.sentAt >= :start)
		AND (:end IS NULL OR e.sentAt < :end)
		AND (:status IS NULL OR e.status = :status)
		AND (:daysBeforePayment IS NULL OR e.daysBeforePayment = :daysBeforePayment)
		AND (:email IS NULL OR e.recipientEmail LIKE %:email%)
		""")
	Page<String> findDistinctRecipientEmails(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	);

	@Query("""
		SELECT e
		FROM EmailNotification e
		WHERE e.recipientEmail in :emails
		AND (:start IS NULL OR e.sentAt >= :start)
		AND (:end IS NULL OR e.sentAt < :end)
		AND (:status IS NULL OR e.status = :status)
		AND (:daysBeforePayment IS NULL OR e.daysBeforePayment = :daysBeforePayment)
		AND (:email IS NULL OR e.recipientEmail LIKE %:email%)
		""")
	List<EmailNotification> findEmailNotifications(
		List<String> emails,
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email
	);
}
