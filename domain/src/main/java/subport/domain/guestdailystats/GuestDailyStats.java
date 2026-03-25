package subport.domain.guestdailystats;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guest_daily_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GuestDailyStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;

	private int count;

	public GuestDailyStats(
		LocalDate date,
		int count
	) {
		this.date = date;
		this.count = count;
	}
}
