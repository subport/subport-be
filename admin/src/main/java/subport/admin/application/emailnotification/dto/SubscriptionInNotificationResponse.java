package subport.admin.application.emailnotification.dto;

public record SubscriptionInNotificationResponse(
	String logoImageUrl,
	String name,
	String amount,
	String amountUnit
) {
}
