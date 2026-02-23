package subport.admin.application.subscription.dto;

public record RegisterSubscriptionRequest(
	String name,
	String type,
	String planUrl
) {
}
