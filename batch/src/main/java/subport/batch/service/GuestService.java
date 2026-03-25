package subport.batch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.batch.persistence.SpringDataGuestDailyStatsRepository;
import subport.batch.persistence.SpringDataMemberRepository;
import subport.domain.guestdailystats.GuestDailyStats;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GuestService {

	private final SpringDataMemberRepository memberRepository;
	private final SpringDataGuestDailyStatsRepository guestDailyStatsRepository;

	public void processGuestDailyStats(LocalDate yesterday) {
		LocalDateTime start = yesterday.atStartOfDay();
		LocalDateTime end = start.plusDays(1);

		int yesterdayGuestCount = memberRepository.countYesterdayGuests(start, end);

		GuestDailyStats guestDailyStats = new GuestDailyStats(yesterday, yesterdayGuestCount);
		guestDailyStatsRepository.save(guestDailyStats);

		int deletedCount = memberRepository.deleteGuestsBefore(end);

		log.info("게스트 일별 집계 완료 - 기준 날짜: {}, 생성 수: {}, 삭제 수: {}",
			yesterday, yesterdayGuestCount, deletedCount);
	}
}
