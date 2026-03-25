package subport.admin.application.emailnotification.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import subport.domain.emailnotification.SendingStatus;

public record EmailNotificationResponse(
	String email,
	long subscriptionCount,
	LocalDate paymentDate,
	Integer daysBeforePayment,
	SendingStatus status,
	int retryCount,
	LocalDateTime sentAt
) {
}
