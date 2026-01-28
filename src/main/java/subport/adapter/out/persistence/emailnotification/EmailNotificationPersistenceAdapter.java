package subport.adapter.out.persistence.emailnotification;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.out.LoadEmailNotificationPort;
import subport.application.emailnotification.port.out.SaveEmailNotificationsPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements
	SaveEmailNotificationsPort,
	LoadEmailNotificationPort {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;

	@Override
	public void save(List<EmailNotification> emailNotifications) {
		emailNotificationRepository.saveAll(emailNotifications);
	}

	@Override
	public List<EmailNotification> loadEmailNotifications(LocalDate currentDate, SendingStatus status) {
		return emailNotificationRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndStatus(
			currentDate.atStartOfDay(),
			currentDate.plusDays(1).atStartOfDay(),
			status
		);
	}
}
