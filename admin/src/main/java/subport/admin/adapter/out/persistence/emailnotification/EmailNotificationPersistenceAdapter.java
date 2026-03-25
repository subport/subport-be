package subport.admin.adapter.out.persistence.emailnotification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.emailnotification.EmailNotificationPort;
import subport.admin.application.emailnotification.dto.EmailNotificationDetailResponse;
import subport.admin.application.emailnotification.dto.EmailNotificationResponse;
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
	public Page<EmailNotificationResponse> loadEmailNotificationGroups(
		LocalDateTime start,
		LocalDateTime end,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	) {
		return emailNotificationRepository.findEmailNotificationGroups(
			start,
			end,
			status,
			daysBeforePayment,
			email,
			pageable
		);
	}

	@Override
	public List<EmailNotificationDetailResponse> loadEmailNotificationDetails(
		String email,
		LocalDate paymentDate,
		Integer daysBeforePayment
	) {
		return emailNotificationRepository.findByGroupKey(email, paymentDate, daysBeforePayment);
	}
}
