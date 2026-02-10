package subport.admin.application.dto;

public record AdminUpdateSubscriptionRequest(
	String name,
	String type,
	String planUrl
) {
}
