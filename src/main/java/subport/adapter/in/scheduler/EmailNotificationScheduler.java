package subport.adapter.in.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.in.CreateEmailNotificationsUseCase;
import subport.application.emailnotification.port.in.RetryFailedEmailNotificationsUseCase;
import subport.application.emailnotification.port.in.SendEmailNotificationsUseCase;

@Component
@RequiredArgsConstructor
public class EmailNotificationScheduler {

	private final CreateEmailNotificationsUseCase createEmailNotificationsUseCase;
	private final SendEmailNotificationsUseCase sendEmailNotificationsUseCase;
	private final RetryFailedEmailNotificationsUseCase retryFailedEmailNotificationsUseCase;

	@Scheduled(cron = "0 50 08 * * *")
	public void createEmailNotifications() {
		createEmailNotificationsUseCase.create(LocalDate.now());
	}

	@Scheduled(cron = "0 0 09 * * *")
	public void sendEmailNotifications() {
		sendEmailNotificationsUseCase.send(LocalDateTime.now());
	}

	@Scheduled(cron = "0 5-15/5 09 * * *")
	public void retryFailedEmailNotifications() {
		retryFailedEmailNotificationsUseCase.retry(LocalDateTime.now());
	}
}
