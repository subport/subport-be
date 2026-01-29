package subport.adapter.in.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.spendingrecord.port.in.CreateSpendingRecordUseCase;

@Component
@RequiredArgsConstructor
public class SpendingRecordScheduler {

	private final CreateSpendingRecordUseCase createSpendingRecordUseCase;

	@Scheduled(cron = "0 0 0 * * *")
	public void run() {
		createSpendingRecordUseCase.create(LocalDateTime.now());
	}
}
