package subport.admin.application.guestdailystats;

import java.time.LocalDate;

import subport.domain.guestdailystats.GuestDailyStats;

public interface GuestDailyStatsPort {

	GuestDailyStats loadGuestDailyStats(LocalDate date);
}
