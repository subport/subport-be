package subport.application.emailnotification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.out.LoadEmailNotificationPort;
import subport.application.emailnotification.port.out.UpdateEmailNotificationPort;
import subport.domain.emailnotification.EmailNotification;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailResultHandler {

	private final LoadEmailNotificationPort loadEmailNotificationPort;
	private final UpdateEmailNotificationPort updateEmailNotificationPort;

	public void success(EmailNotification notification, LocalDateTime currentDateTime) {
		EmailNotification emailNotification = loadEmailNotificationPort.loadEmailNotification(notification.getId());
		emailNotification.markSent();
		emailNotification.updateSentAt(currentDateTime);

		updateEmailNotificationPort.update(emailNotification);
	}

	public void fail(EmailNotification notification) {
		EmailNotification emailNotification = loadEmailNotificationPort.loadEmailNotification(notification.getId());
		emailNotification.markFailed();

		updateEmailNotificationPort.update(emailNotification);
	}
}
