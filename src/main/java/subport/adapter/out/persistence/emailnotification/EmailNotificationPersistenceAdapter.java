package subport.adapter.out.persistence.emailnotification;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.out.LoadEmailNotificationPort;
import subport.application.emailnotification.port.out.SaveEmailNotificationsPort;
import subport.application.emailnotification.port.out.UpdateEmailNotificationPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Component
@RequiredArgsConstructor
public class EmailNotificationPersistenceAdapter implements
	SaveEmailNotificationsPort,
	LoadEmailNotificationPort,
	UpdateEmailNotificationPort {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;
	private final EmailNotificationMapper emailNotificationMapper;

	@Override
	public void save(List<EmailNotification> emailNotifications) {
		List<EmailNotificationJpaEntity> entities = emailNotifications.stream()
			.map(emailNotificationMapper::toJpaEntity)
			.toList();

		emailNotificationRepository.saveAll(entities);
	}

	@Override
	public EmailNotification loadEmailNotification(Long id) {
		return emailNotificationMapper.toDomain(
			emailNotificationRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOTIFICATION_NOT_FOUND))
		);
	}

	@Override
	public List<EmailNotification> loadEmailNotifications(LocalDate currentDate, SendingStatus status) {
		return emailNotificationRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanAndStatus(
				currentDate.atStartOfDay(),
				currentDate.plusDays(1).atStartOfDay(),
				status
			).stream()
			.map(emailNotificationMapper::toDomain)
			.toList();
	}

	@Override
	public void update(EmailNotification emailNotification) {
		EmailNotificationJpaEntity emailNotificationEntity =
			emailNotificationRepository.findById(emailNotification.getId())
				.orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOTIFICATION_NOT_FOUND));

		emailNotificationEntity.apply(emailNotification);
	}
}
