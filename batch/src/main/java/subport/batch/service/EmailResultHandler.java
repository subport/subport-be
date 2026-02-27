package subport.batch.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import subport.domain.emailnotification.EmailNotification;

@Component
@Transactional
public class EmailResultHandler {

	public void handleSuccess(
		List<EmailNotification> emailNotifications,
		LocalDateTime now,
		boolean isRetry
	) {
		if (isRetry) {
			emailNotifications.forEach(EmailNotification::increaseRetryCount);
		}

		emailNotifications.forEach(notification -> {
			if (isRetry) {
				notification.increaseRetryCount();
			}
			notification.markSent(now);
		});
	}

	public void handleFailure(
		List<EmailNotification> emailNotifications,
		LocalDateTime now,
		boolean isRetry
	) {
		emailNotifications.forEach(notification -> {
			if (isRetry) {
				notification.increaseRetryCount();
			} else {
				notification.markFailed(now);
			}
		});
	}
}
