package subport.application.emailnotification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.in.RetryFailedEmailNotificationsUseCase;
import subport.application.emailnotification.port.out.LoadEmailNotificationPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Service
@RequiredArgsConstructor
public class RetryFailedEmailNotificationsService implements RetryFailedEmailNotificationsUseCase {

	private final EmailSender emailSender;
	private final LoadEmailNotificationPort loadEmailNotificationPort;

	@Override
	public void retry(LocalDateTime currentDateTime) {
		List<EmailNotification> emailNotifications =
			loadEmailNotificationPort.loadEmailNotifications(currentDateTime.toLocalDate(), SendingStatus.FAILED);

		if (emailNotifications.isEmpty()) {
			return;
		}

		for (EmailNotification emailNotification : emailNotifications) {
			emailSender.sendAsync(emailNotification, true, currentDateTime);
		}
	}
}
