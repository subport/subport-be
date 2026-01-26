package subport.adapter.out.persistence.emailnotification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.emailnotification.SendingStatus;

public interface SpringDataEmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {

	List<EmailNotificationJpaEntity> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndStatus(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status
	);
}
