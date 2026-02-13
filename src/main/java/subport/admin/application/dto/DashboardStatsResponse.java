package subport.admin.application.dto;

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
