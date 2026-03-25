package subport.batch.scheduler;

import java.time.LocalDate;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.batch.service.GuestService;

@Component
@RequiredArgsConstructor
public class GuestScheduler {

	private final GuestService guestService;

	@Scheduled(cron = "0 00 04 * * *")
	public void processGuestDailyStats() {
		try {
			MDC.put("jobId", UUID.randomUUID().toString().substring(0, 8));
			guestService.processGuestDailyStats(LocalDate.now().minusDays(1));
		} finally {
			MDC.clear();
		}
	}
}
