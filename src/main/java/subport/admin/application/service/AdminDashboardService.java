package subport.admin.application.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import subport.admin.application.dto.DashboardTodayEmailNotificationResponse;
import subport.admin.application.dto.DashboardTodayEmailNotificationsResponse;
import subport.admin.application.dto.DashboardTopCustomSubscriptionsResponse;
import subport.admin.application.dto.DashboardTopSubscriptionsResponse;
import subport.admin.application.port.AdminEmailNotificationPort;
import subport.admin.application.port.AdminMemberPort;
import subport.admin.application.port.AdminMemberSubscriptionPort;
import subport.admin.application.query.EmailStatusCount;
import subport.admin.application.query.MemberSubscriptionCount;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminDashboardService {

	private final AdminMemberPort memberPort;
	private final AdminMemberSubscriptionPort memberSubscriptionPort;
	private final AdminEmailNotificationPort emailNotificationPort;

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
			memberSubscriptionPort.loadTopSubscriptions()
		);
	}

	public DashboardTopCustomSubscriptionsResponse getTopCustomSubscriptions() {
		return new DashboardTopCustomSubscriptionsResponse(memberSubscriptionPort.loadTopCustomSubscriptions());
	}

	public DashboardTodayEmailNotificationsResponse getTodayEmailNotifications(LocalDate today) {
		Map<SendingStatus, Long> map = emailNotificationPort.countTodayByStatus(today).stream()
			.collect(Collectors.toMap(
				EmailStatusCount::status,
				EmailStatusCount::count
			));

		Long successCount = map.getOrDefault(SendingStatus.SENT, 0L);
		Long failedCount = map.getOrDefault(SendingStatus.FAILED, 0L);
		Long pendingCount = map.getOrDefault(SendingStatus.PENDING, 0L);

		Map<String, List<EmailNotification>> grouped = emailNotificationPort.loadEmailNotifications(today).stream()
			.collect(Collectors.groupingBy(EmailNotification::getRecipientEmail));

		List<DashboardTodayEmailNotificationResponse> notifications = grouped.entrySet().stream()
			.map(entry -> {
				String recipientEmail = entry.getKey();

				List<EmailNotification> value = entry.getValue();
				int subscriptionCount = value.size();

				EmailNotification emailNotification = value.get(0);
				Integer daysBeforePayment = emailNotification.getDaysBeforePayment();
				SendingStatus status = emailNotification.getStatus();
				LocalDateTime sentAt = emailNotification.getSentAt();

				return new DashboardTodayEmailNotificationResponse(
					recipientEmail,
					subscriptionCount,
					daysBeforePayment,
					status,
					sentAt
				);
			})
			.toList();

		return new DashboardTodayEmailNotificationsResponse(
			notifications,
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
