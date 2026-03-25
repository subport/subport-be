package subport.batch.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.guestdailystats.GuestDailyStats;

public interface SpringDataGuestDailyStatsRepository extends JpaRepository<GuestDailyStats, Long> {
}
