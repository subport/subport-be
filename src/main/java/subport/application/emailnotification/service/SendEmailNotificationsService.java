package subport.application.emailnotification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.in.SendEmailNotificationsUseCase;
import subport.application.emailnotification.port.out.LoadEmailNotificationPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Service
@RequiredArgsConstructor
public class SendEmailNotificationsService implements SendEmailNotificationsUseCase {

	private final EmailSender emailSender;
	private final LoadEmailNotificationPort loadEmailNotificationPort;

	@Override
	public void send(LocalDateTime currentDateTime) {
		List<EmailNotification> emailNotifications =
			loadEmailNotificationPort.loadEmailNotifications(currentDateTime.toLocalDate(), SendingStatus.PENDING);

		for (EmailNotification emailNotification : emailNotifications) {
			emailSender.sendAsync(emailNotification, false, currentDateTime);
		}
	}
}
