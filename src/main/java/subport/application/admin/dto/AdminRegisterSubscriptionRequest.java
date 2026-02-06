package subport.application.admin.dto;

public record AdminRegisterSubscriptionRequest(
	String name,
	String type,
	String planUrl
) {
}
