package subport.admin.application.subscription.dto;

public record UpdateSubscriptionRequest(
	String name,
	String type,
	String planUrl
) {
}
