package subport.batch.scheduler;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.batch.service.RefreshTokenService;

@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {

	private final RefreshTokenService refreshTokenService;

	@Scheduled(cron = "0 15 04 * * *")
	public void deleteExpiredRefreshTokens() {
		try {
			MDC.put("jobId", UUID.randomUUID().toString().substring(0, 8));
			refreshTokenService.deleteExpiredRefreshTokens(Instant.now());
		} finally {
			MDC.clear();
		}
	}
}
