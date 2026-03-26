package subport.admin.adapter.out.persistence.guestdailystats;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import subport.domain.guestdailystats.GuestDailyStats;

public interface SpringDataGuestDailyStatsRepository extends JpaRepository<GuestDailyStats, Long> {

	Optional<GuestDailyStats> findByDate(LocalDate date);
}
