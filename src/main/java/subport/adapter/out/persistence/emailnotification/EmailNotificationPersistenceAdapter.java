package subport.adapter.out.persistence.emailnotification;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.out.SaveEmailNotificationsPort;
import subport.domain.emailnotification.EmailNotification;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements SaveEmailNotificationsPort {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;
	private final EmailNotificationMapper emailNotificationMapper;

	@Override
	public void save(List<EmailNotification> emailNotifications) {
		List<EmailNotificationJpaEntity> entities = emailNotifications.stream()
			.map(emailNotificationMapper::toJpaEntity)
			.toList();

		emailNotificationRepository.saveAll(entities);
	}
}
