package subport.admin.adapter.out.persistence.emailnotification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.emailnotification.EmailNotificationPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements EmailNotificationPort {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;

	@Override
	public List<EmailNotification> loadEmailNotifications(LocalDate date) {
		return emailNotificationRepository.findByCreatedAt(
			date.atStartOfDay(),
			date.plusDays(1).atStartOfDay()
		);
	}

	@Override
	public Page<String> searchDistinctRecipientEmails(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	) {
		return emailNotificationRepository.findDistinctRecipientEmails(
			start,
			end,
			status,
			daysBeforePayment,
			email,
			pageable
		);
	}

	@Override
	public List<EmailNotification> searchEmailNotifications(
		List<String> emails,
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email
	) {
		return emailNotificationRepository.findEmailNotifications(
			emails,
			start,
			end,
			status,
			daysBeforePayment,
			email
		);
	}
}
