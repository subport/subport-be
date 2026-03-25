package subport.admin.application.emailnotification.dto;

public record EmailNotificationDetailResponse(
	String logoImageUrl,
	String name,
	String amount,
	String amountUnit
) {
}
