package subport.admin.adapter.out.persistence.emailnotification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import subport.admin.application.emailnotification.dto.EmailNotificationResponse;
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
		SELECT new subport.admin.application.emailnotification.dto.EmailNotificationResponse(
		        e.recipientEmail,
		        COUNT(e),
		        e.paymentDate,
		        e.daysBeforePayment,
		        e.status,
		        e.retryCount,
		        e.sentAt
		    )
		FROM EmailNotification e
		WHERE (:start IS NULL OR e.sentAt >= :start)
		AND (:end IS NULL OR e.sentAt < :end)
		AND (:status IS NULL OR e.status = :status)
		AND (:daysBeforePayment IS NULL OR e.daysBeforePayment = :daysBeforePayment)
		AND (:email IS NULL OR e.recipientEmail LIKE %:email%)
		GROUP BY e.recipientEmail, e.paymentDate, e.daysBeforePayment, e.status, e.retryCount, e.sentAt
		""")
	Page<EmailNotificationResponse> findEmailNotificationGroups(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	);
}
