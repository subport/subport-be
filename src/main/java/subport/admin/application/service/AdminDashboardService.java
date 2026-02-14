package subport.admin.application.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.DashboardRecentMemberResponse;
import subport.admin.application.dto.DashboardRecentMembersResponse;
import subport.admin.application.dto.DashboardSignupTrendResponse;
import subport.admin.application.dto.DashboardSignupTrendsResponse;
import subport.admin.application.dto.DashboardStatsResponse;
import subport.admin.application.dto.DashboardTopCustomSubscriptionResponse;
import subport.admin.application.dto.DashboardTopCustomSubscriptionsResponse;
import subport.admin.application.dto.DashboardTopServicesResponse;
import subport.admin.application.port.AdminMemberPort;
import subport.admin.application.port.AdminMemberSubscriptionPort;
import subport.admin.application.query.CustomMemberSubscriptionCount;
import subport.admin.application.query.MemberSubscriptionCount;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminDashboardService {

	private final AdminMemberPort memberPort;
	private final AdminMemberSubscriptionPort memberSubscriptionPort;

	public DashboardStatsResponse getStats(LocalDate today) {
		long totalMemberCount = memberPort.countMembers();

		DateTimeRange thisWeekRange = weekRange(today);
		long weeklyNewMemberCount = memberPort.countMembers(
			thisWeekRange.start,
			thisWeekRange.end
		);

		DateTimeRange todayRange = dayRange(today);
		long todayNewMemberCount = memberPort.countMembers(
			todayRange.start,
			todayRange.end
		);

		DateTimeRange yesterdayRange = dayRange(today.minusDays(1));
		long yesterdayNewMemberCount = memberPort.countMembers(
			yesterdayRange.start,
			yesterdayRange.end
		);

		long totalMemberSubscriptionCount = memberSubscriptionPort.countActiveMemberSubscriptions();

		long weeklyNewMemberSubscriptionCount = memberSubscriptionPort.countActiveMemberSubscriptions(
			thisWeekRange.start,
			thisWeekRange.end
		);

		DateTimeRange lastMonthRange = monthRange(today);
		long currentActiveMemberCount = memberPort.countActiveMembers(
			lastMonthRange.start,
			lastMonthRange.end
		);

		return new DashboardStatsResponse(
			totalMemberCount,
			weeklyNewMemberCount,
			todayNewMemberCount,
			yesterdayNewMemberCount,
			totalMemberSubscriptionCount,
			weeklyNewMemberSubscriptionCount,
			currentActiveMemberCount
		);
	}

	public DashboardSignupTrendsResponse getSignUpTrend(LocalDate today) {
		DateTimeRange lastTwoWeeks = lastTwoWeeksRange(today);
		Map<LocalDate, Long> grouped = memberPort.loadMembers(
				lastTwoWeeks.start,
				lastTwoWeeks.end
			).stream()
			.collect(Collectors.groupingBy(member -> member.getCreatedAt().toLocalDate(),
				Collectors.counting()
			));

		List<DashboardSignupTrendResponse> signupTrends = new ArrayList<>();

		LocalDate startDate = lastTwoWeeks.start.toLocalDate();
		LocalDate endDate = lastTwoWeeks.end.toLocalDate().minusDays(1);
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			signupTrends.add(
				new DashboardSignupTrendResponse(
					date,
					grouped.getOrDefault(date, 0L)
				)
			);
		}

		return new DashboardSignupTrendsResponse(signupTrends);
	}

	public DashboardRecentMembersResponse getRecentMembers() {
		List<Member> members = memberPort.loadLatestMembers();

		List<Long> memberIds = members.stream()
			.map(Member::getId)
			.toList();

		Map<Long, Long> memberSubscriptionCountMap =
			memberSubscriptionPort.countActiveMemberSubscriptions(memberIds).stream()
				.collect(Collectors.toMap(
					MemberSubscriptionCount::memberId,
					MemberSubscriptionCount::memberSubscriptionCount
				));

		return new DashboardRecentMembersResponse(
			members.stream()
				.map(member -> new DashboardRecentMemberResponse(
					member.getNickname(),
					member.getEmail(),
					memberSubscriptionCountMap.getOrDefault(member.getId(), 0L),
					member.getCreatedAt()
				))
				.toList()
		);
	}

	public DashboardTopServicesResponse getTopServices() {
		return new DashboardTopServicesResponse(
			memberSubscriptionPort.loadTopSubscriptions()
		);
	}

	public DashboardTopCustomSubscriptionsResponse getTopCustomSubscriptions() {
		List<CustomMemberSubscriptionCount> subscriptions = memberSubscriptionPort.loadTopCustomSubscriptions();

		if (subscriptions.isEmpty()) {
			return new DashboardTopCustomSubscriptionsResponse(List.of());
		}

		Map<String, List<CustomMemberSubscriptionCount>> grouped = subscriptions.stream()
			.collect(Collectors.groupingBy(
				CustomMemberSubscriptionCount::normalizedSubscriptionName
			));

		List<DashboardTopCustomSubscriptionResponse> result = grouped.values().stream()
			.map(value -> {
				String representativeName = value.stream()
					.max(Comparator.comparing(CustomMemberSubscriptionCount::memberSubscriptionCount))
					.get()
					.subscriptionName();

				long totalCount = value.stream()
					.mapToLong(CustomMemberSubscriptionCount::memberSubscriptionCount)
					.sum();

				return new DashboardTopCustomSubscriptionResponse(representativeName, totalCount);
			})
			.sorted(Comparator.comparing(DashboardTopCustomSubscriptionResponse::memberSubscriptionCount).reversed())
			.limit(5)
			.toList();

		return new DashboardTopCustomSubscriptionsResponse(result);
	}

	private DateTimeRange dayRange(LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = start.plusDays(1);
		return new DateTimeRange(start, end);
	}

	private DateTimeRange weekRange(LocalDate date) {
		LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
		LocalDateTime start = startOfWeek.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		return new DateTimeRange(start, end);
	}

	private DateTimeRange monthRange(LocalDate date) {
		LocalDateTime start = date.minusMonths(1).atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		return new DateTimeRange(start, end);
	}

	private DateTimeRange lastTwoWeeksRange(LocalDate today) {
		LocalDateTime start = today.minusWeeks(2).plusDays(1).atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();
		return new DateTimeRange(start, end);
	}

	private record DateTimeRange(LocalDateTime start, LocalDateTime end) {
	}
}
