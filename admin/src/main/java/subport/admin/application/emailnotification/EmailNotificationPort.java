package subport.admin.application.emailnotification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import subport.admin.application.emailnotification.dto.EmailNotificationDetailResponse;
import subport.admin.application.emailnotification.dto.EmailNotificationResponse;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

public interface EmailNotificationPort {

	List<EmailNotification> loadEmailNotifications(LocalDate date);

	Page<EmailNotificationResponse> loadEmailNotificationGroups(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	);

	List<EmailNotificationDetailResponse> loadEmailNotificationDetails(
		String email,
		LocalDate paymentDate,
		Integer daysBeforePayment
	);
}
