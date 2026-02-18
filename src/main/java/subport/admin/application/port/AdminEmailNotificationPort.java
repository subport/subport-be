package subport.admin.application.port;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

public interface AdminEmailNotificationPort {

	List<EmailNotification> loadEmailNotifications(LocalDate date);

	Page<String> searchDistinctRecipientEmails(
		LocalDate date,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	);

	List<EmailNotification> searchEmailNotifications(
		List<String> emails,
		LocalDate date,
		SendingStatus status,
		Integer daysBeforePayment,
		String email
	);
}
