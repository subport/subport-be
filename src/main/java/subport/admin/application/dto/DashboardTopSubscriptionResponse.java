package subport.admin.application.dto;

public record DashboardTopSubscriptionResponse(
	String subscriptionName,
	long memberSubscriptionCount
) {
}
