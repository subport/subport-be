package subport.admin.application.dashboard.dto;

public record DashboardStatsResponse(
	long totalMemberCount,
	long weeklyNewMemberCount,
	long todayNewMemberCount,
	long yesterdayNewMemberCount,
	long totalMemberSubscriptionCount,
	long weeklyNewMemberSubscriptionCount,
	long currentActiveMemberCount
) {
}
