package subport.batch.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.batch.service.SpendingRecordService;

@Component
@RequiredArgsConstructor
public class SpendingRecordScheduler {

	private final SpendingRecordService spendingRecordService;

	@Scheduled(cron = "0 0 0 * * *")
	public void run() {
		spendingRecordService.createForScheduling(LocalDateTime.now());
	}
}
