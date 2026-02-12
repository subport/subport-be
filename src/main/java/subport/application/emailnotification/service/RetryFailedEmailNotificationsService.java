package subport.application.emailnotification.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

		Map<String, List<EmailNotification>> groupedEmailNotifications = emailNotifications.stream()
			.collect(Collectors.groupingBy(EmailNotification::getRecipientEmail));

		for (Map.Entry<String, List<EmailNotification>> entry : groupedEmailNotifications.entrySet()) {
			emailSender.sendAsync(entry.getValue(), true, currentDateTime);
		}
	}
}
