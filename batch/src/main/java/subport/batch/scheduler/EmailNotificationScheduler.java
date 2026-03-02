package subport.batch.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.batch.service.EmailNotificationService;

@Component
@RequiredArgsConstructor
public class EmailNotificationScheduler {

	private final EmailNotificationService emailNotificationService;

	@Scheduled(cron = "0 30 06 * * *")
	public void createEmailNotifications() {
		try {
			MDC.put("jobId", UUID.randomUUID().toString().substring(0, 8));
			emailNotificationService.create(LocalDate.now());
		} finally {
			MDC.clear();
		}
	}

	@Scheduled(cron = "0 0 07 * * *")
	public void sendEmailNotifications() {
		try {
			MDC.put("jobId", UUID.randomUUID().toString().substring(0, 8));
			emailNotificationService.send(LocalDateTime.now());
		} finally {
			MDC.clear();
		}
	}

	@Scheduled(cron = "0 5-15/5 07 * * *")
	public void retryFailedEmailNotifications() {
		try {
			MDC.put("jobId", UUID.randomUUID().toString().substring(0, 8));
			emailNotificationService.retry(LocalDateTime.now());
		} finally {
			MDC.clear();
		}
	}
}
