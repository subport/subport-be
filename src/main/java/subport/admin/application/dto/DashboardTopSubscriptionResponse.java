package subport.admin.application.dto;

public record DashboardTopSubscriptionResponse(
	String subscriptionName,
	String subscriptionLogoImageUrl,
	long memberSubscriptionCount
) {
}
