package subport.admin.application.dto;

public record DashboardTopServiceResponse(
	String subscriptionName,
	long memberSubscriptionCount
) {
}
