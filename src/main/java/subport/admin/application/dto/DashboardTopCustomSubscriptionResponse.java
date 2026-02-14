package subport.admin.application.dto;

public record DashboardTopCustomSubscriptionResponse(
	String subscriptionName,
	long memberSubscriptionCount
) {
}
