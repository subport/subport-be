package subport.admin.application.dto;

public record DashboardTopCustomSubscriptionResponse(
	String normalizedSubscriptionName,
	long memberSubscriptionCount
) {
}
