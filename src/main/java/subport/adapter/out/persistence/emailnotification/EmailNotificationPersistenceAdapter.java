package subport.adapter.out.persistence.emailnotification;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminEmailNotificationPort;
import subport.admin.application.query.EmailStatusCount;
import subport.application.emailnotification.port.out.LoadEmailNotificationPort;
import subport.application.emailnotification.port.out.SaveEmailNotificationsPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements
	SaveEmailNotificationsPort,
	LoadEmailNotificationPort,
	AdminEmailNotificationPort {

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

	@Override
	public List<EmailNotification> loadEmailNotifications(LocalDate date) {
		return emailNotificationRepository.findByCreatedAt(
			date.atStartOfDay(),
			date.plusDays(1).atStartOfDay()
		);
	}

	@Override
	public List<EmailStatusCount> countTodayByStatus(LocalDate date) {
		return emailNotificationRepository.countTodayByStatus(
			date.atStartOfDay(),
			date.plusDays(1).atStartOfDay()
		);
	}
}
