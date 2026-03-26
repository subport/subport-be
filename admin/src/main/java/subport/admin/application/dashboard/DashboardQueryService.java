package subport.admin.application.dashboard;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dashboard.dto.DashboardRecentMemberResponse;
import subport.admin.application.dashboard.dto.DashboardRecentMembersResponse;
import subport.admin.application.dashboard.dto.DashboardSignupTrendResponse;
import subport.admin.application.dashboard.dto.DashboardSignupTrendsResponse;
import subport.admin.application.dashboard.dto.DashboardStatsResponse;
import subport.admin.application.dashboard.dto.DashboardTodayEmailNotificationResponse;
import subport.admin.application.dashboard.dto.DashboardTodayEmailNotificationsResponse;
import subport.admin.application.dashboard.dto.DashboardTopCustomSubscriptionsResponse;
import subport.admin.application.dashboard.dto.DashboardTopSubscriptionsResponse;
import subport.admin.application.emailnotification.EmailNotificationPort;
import subport.admin.application.guestdailystats.GuestDailyStatsPort;
import subport.admin.application.member.MemberPort;
import subport.admin.application.membersubscription.MemberSubscriptionPort;
import subport.admin.application.membersubscription.dto.MemberSubscriptionCount;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;
import subport.domain.guestdailystats.GuestDailyStats;
import subport.domain.member.Member;
import subport.domain.member.MemberRole;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardQueryService {

	private final MemberPort memberPort;
	private final MemberSubscriptionPort memberSubscriptionPort;
	private final EmailNotificationPort emailNotificationPort;
	private final GuestDailyStatsPort guestDailyStatsPort;

	public DashboardStatsResponse getStats(LocalDate today) {
		long totalMemberCount = memberPort.countMembers();

		DateTimeRange thisWeekRange = weekRange(today);
		long weeklyNewMemberCount = memberPort.countMembers(
			thisWeekRange.start,
			thisWeekRange.end,
			MemberRole.MEMBER
		);

		DateTimeRange todayRange = dayRange(today);
		long todayNewMemberCount = memberPort.countMembers(
			todayRange.start,
			todayRange.end,
			MemberRole.MEMBER
		);

		DateTimeRange yesterdayRange = dayRange(today.minusDays(1));
		long yesterdayNewMemberCount = memberPort.countMembers(
			yesterdayRange.start,
			yesterdayRange.end,
			MemberRole.MEMBER
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

		long todayGuestCount = memberPort.countMembers(
			todayRange.start,
			todayRange.end,
			MemberRole.GUEST
		);

		GuestDailyStats guestDailyStats = guestDailyStatsPort.loadGuestDailyStats(today.minusDays(1));
		long yesterdayGuestCount;
		if (guestDailyStats != null) {
			yesterdayGuestCount = guestDailyStats.getCount();
		} else {
			yesterdayGuestCount = memberPort.countMembers(
				yesterdayRange.start,
				yesterdayRange.end,
				MemberRole.GUEST
			);
		}

		return new DashboardStatsResponse(
			totalMemberCount,
			weeklyNewMemberCount,
			todayNewMemberCount,
			yesterdayNewMemberCount,
			totalMemberSubscriptionCount,
			weeklyNewMemberSubscriptionCount,
			currentActiveMemberCount,
			todayGuestCount,
			yesterdayGuestCount
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
		List<Member> members = memberPort.loadRecentMembers(PageRequest.of(0, 4));

		List<Long> memberIds = members.stream()
			.map(Member::getId)
			.toList();

		Map<Long, Long> memberSubscriptionCountMap =
			memberSubscriptionPort.countActiveMemberSubscriptions(memberIds, null).stream()
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

	public DashboardTopSubscriptionsResponse getTopSubscriptions() {
		return new DashboardTopSubscriptionsResponse(
			memberSubscriptionPort.loadTopSubscriptions(PageRequest.of(0, 5))
		);
	}

	public DashboardTopCustomSubscriptionsResponse getTopCustomSubscriptions() {
		return new DashboardTopCustomSubscriptionsResponse(
			memberSubscriptionPort.loadTopCustomSubscriptions(PageRequest.of(0, 5))
		);
	}

	public DashboardTodayEmailNotificationsResponse getTodayEmailNotifications(LocalDate today) {
		Map<String, List<EmailNotification>> grouped = emailNotificationPort.loadEmailNotifications(today).stream()
			.collect(Collectors.groupingBy(EmailNotification::getRecipientEmail));

		List<DashboardTodayEmailNotificationResponse> items = grouped.entrySet().stream()
			.map(entry -> {
				String recipientEmail = entry.getKey();
				List<EmailNotification> notifications = entry.getValue();
				EmailNotification notification = notifications.get(0);

				return new DashboardTodayEmailNotificationResponse(
					recipientEmail,
					notifications.size(),
					notification.getDaysBeforePayment(),
					notification.getStatus(),
					notification.getSentAt()
				);
			})
			.sorted(Comparator.comparing(DashboardTodayEmailNotificationResponse::sentAt).reversed())
			.toList();

		Map<SendingStatus, Long> statusCountMap = items.stream()
			.collect(Collectors.groupingBy(
				DashboardTodayEmailNotificationResponse::status,
				Collectors.counting()
			));

		long successCount = statusCountMap.getOrDefault(SendingStatus.SENT, 0L);
		long failedCount = statusCountMap.getOrDefault(SendingStatus.FAILED, 0L);
		long pendingCount = statusCountMap.getOrDefault(SendingStatus.PENDING, 0L);

		return new DashboardTodayEmailNotificationsResponse(
			items.subList(0, Math.min(7, items.size())),
			successCount,
			failedCount,
			pendingCount
		);
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
