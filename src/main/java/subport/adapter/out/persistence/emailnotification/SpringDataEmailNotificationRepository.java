package subport.adapter.out.persistence.emailnotification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataEmailNotificationRepository extends JpaRepository<EmailNotificationJpaEntity, Long> {
}
