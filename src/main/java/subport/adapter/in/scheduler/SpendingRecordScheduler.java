package subport.adapter.in.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.spendingrecord.port.in.CreateSpendingRecordUseCase;

@Component
@RequiredArgsConstructor
public class SpendingRecordScheduler {

	private final CreateSpendingRecordUseCase createSpendingRecordUseCase;

	@Scheduled(cron = "0 0 12 * * *")
	public void run() {
		createSpendingRecordUseCase.create();
	}
}
