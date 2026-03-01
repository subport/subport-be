package subport.batch.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.batch.persistence.SpringDataEmailNotificationRepository;
import subport.domain.emailnotification.EmailNotification;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailResultHandler {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;

	public void handleSuccess(
		List<Long> ids,
		LocalDateTime now,
		boolean isRetry
	) {
		List<EmailNotification> emailNotifications = emailNotificationRepository.findAllById(ids);
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
		List<Long> ids,
		LocalDateTime now,
		boolean isRetry
	) {
		List<EmailNotification> emailNotifications = emailNotificationRepository.findAllById(ids);
		emailNotifications.forEach(notification -> {
			if (isRetry) {
				notification.increaseRetryCount();
			} else {
				notification.markFailed(now);
			}
		});
	}
}
