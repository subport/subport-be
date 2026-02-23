package subport.admin.application.dashboard.dto;

public record DashboardTopCustomSubscriptionResponse(
	String normalizedSubscriptionName,
	long memberSubscriptionCount
) {
}
