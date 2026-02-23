package subport.admin.application.dashboard.dto;

public record DashboardTopSubscriptionResponse(
	String subscriptionName,
	String subscriptionLogoImageUrl,
	long memberSubscriptionCount
) {
}
