package subport.admin.application.dto;

public record AdminSubscriptionInNotificationResponse(
	String logoImageUrl,
	String name,
	String amount,
	String amountUnit
) {
}
