package subport.application.admin.dto;

public record AdminUpdateSubscriptionRequest(
	String name,
	String type,
	String planUrl
) {
}
