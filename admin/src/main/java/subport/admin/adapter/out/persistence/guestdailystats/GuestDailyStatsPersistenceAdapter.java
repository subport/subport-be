package subport.admin.adapter.out.persistence.guestdailystats;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.guestdailystats.GuestDailyStatsPort;
import subport.domain.guestdailystats.GuestDailyStats;

@Component
@RequiredArgsConstructor
public class GuestDailyStatsPersistenceAdapter implements GuestDailyStatsPort {

	private final SpringDataGuestDailyStatsRepository guestDailyStatsRepository;

	@Override
	public GuestDailyStats loadGuestDailyStats(LocalDate date) {
		return guestDailyStatsRepository.findByDate(date)
			.orElse(null);
	}
}
