package subport.admin.application.dashboard.dto;

import java.time.LocalDateTime;

import subport.domain.emailnotification.SendingStatus;

public record DashboardTodayEmailNotificationResponse(
	String recipientEmail,
	int subscriptionCount,
	Integer daysBeforePayment,
	SendingStatus status,
	LocalDateTime sentAt
) {
}
