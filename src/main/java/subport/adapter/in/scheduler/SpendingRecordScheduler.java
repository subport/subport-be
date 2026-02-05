package subport.adapter.in.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.spendingrecord.port.in.CreateSpendingRecordsUseCase;

@Component
@RequiredArgsConstructor
public class SpendingRecordScheduler {

	private final CreateSpendingRecordsUseCase createSpendingRecordsUseCase;

	@Scheduled(cron = "0 0 0 * * *")
	public void run() {
		createSpendingRecordsUseCase.createForScheduling(LocalDateTime.now());
	}
}
