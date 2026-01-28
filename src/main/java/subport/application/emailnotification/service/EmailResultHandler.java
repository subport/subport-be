package subport.application.emailnotification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.domain.emailnotification.EmailNotification;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailResultHandler {

	public void handleSuccess(
		EmailNotification emailNotification,
		LocalDateTime currentDateTime,
		boolean isRetry
	) {
		if (isRetry) {
			emailNotification.increaseRetryCount();
		}
		emailNotification.markSent();
		emailNotification.updateSentAt(currentDateTime);
	}

	public void handleFailure(EmailNotification emailNotification, boolean isRetry) {
		if (isRetry) {
			emailNotification.increaseRetryCount();
		}
		if (!isRetry) {
			emailNotification.markFailed();
		}
	}
}
