package subport.batch.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.batch.service.EmailNotificationService;

@Component
@RequiredArgsConstructor
public class EmailNotificationScheduler {

	private final EmailNotificationService emailNotificationService;

	@Scheduled(cron = "0 30 08 * * *")
	public void createEmailNotifications() {
		emailNotificationService.create(LocalDate.now());
	}

	@Scheduled(cron = "0 0 09 * * *")
	public void sendEmailNotifications() {
		emailNotificationService.send(LocalDateTime.now());
	}

	@Scheduled(cron = "0 30-45/5 09 * * *")
	public void retryFailedEmailNotifications() {
		emailNotificationService.retry(LocalDateTime.now());
	}
}
