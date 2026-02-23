package subport.admin.application.emailnotification.dto;

public record AdminSubscriptionInNotificationResponse(
	String logoImageUrl,
	String name,
	String amount,
	String amountUnit
) {
}
