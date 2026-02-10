package subport.admin.application.dto;

public record AdminRegisterSubscriptionRequest(
	String name,
	String type,
	String planUrl
) {
}
