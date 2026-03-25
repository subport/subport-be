package subport.batch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.batch.persistence.SpringDataGuestDailyStatsRepository;
import subport.batch.persistence.SpringDataMemberRepository;
import subport.batch.persistence.SpringDataMemberSubscriptionRepository;
import subport.batch.persistence.SpringDataPlanRepository;
import subport.batch.persistence.SpringDataSpendingRecordRepository;
import subport.batch.persistence.SpringDataSubscriptionRepository;
import subport.domain.guestdailystats.GuestDailyStats;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GuestService {

	private final SpringDataMemberRepository memberRepository;
	private final SpringDataGuestDailyStatsRepository guestDailyStatsRepository;
	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;
	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SpringDataPlanRepository planRepository;
	private final SpringDataSpendingRecordRepository spendingRecordRepository;

	public void processGuestDailyStats(LocalDate yesterday) {
		LocalDateTime start = yesterday.atStartOfDay();
		LocalDateTime end = start.plusDays(1);

		List<Member> guests = memberRepository.getGuestsBefore(end);

		int yesterdayGuestCount = (int)guests.stream()
			.filter(g -> !g.getCreatedAt().isBefore(start) && g.getCreatedAt().isBefore(end))
			.count();

		GuestDailyStats guestDailyStats = new GuestDailyStats(yesterday, yesterdayGuestCount);
		guestDailyStatsRepository.save(guestDailyStats);

		List<Long> guestIds = guests.stream()
			.map(Member::getId)
			.toList();

		memberSubscriptionRepository.deleteAllByMemberIds(guestIds);
		planRepository.deleteAllByMemberIds(guestIds);
		subscriptionRepository.deleteAllByMemberIds(guestIds);
		spendingRecordRepository.deleteAllByMemberIds(guestIds);
		memberRepository.deleteAllByIdInBatch(guestIds);

		log.info("게스트 일별 집계 완료 - 기준 날짜: {}, 생성 수: {}, 삭제 수: {}",
			yesterday, yesterdayGuestCount, guests.size());
	}
}
