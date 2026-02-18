package subport.admin.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import subport.domain.emailnotification.SendingStatus;

public record AdminEmailNotificationResponse(
	String email,
	LocalDate paymentDate,
	Integer daysBeforePayment,
	SendingStatus status,
	LocalDateTime sentAt,
	List<AdminSubscriptionInNotificationResponse> subscriptions
) {
}
