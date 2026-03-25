package subport.admin.application.emailnotification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.emailnotification.dto.EmailNotificationDetailResponse;
import subport.admin.application.emailnotification.dto.EmailNotificationResponse;
import subport.admin.application.emailnotification.dto.EmailNotificationsResponse;
import subport.domain.emailnotification.SendingStatus;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailNotificationQueryService {

	private final EmailNotificationPort emailNotificationPort;

	public EmailNotificationsResponse searchEmailNotifications(
		LocalDate date,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	) {
		LocalDateTime start = null;
		LocalDateTime end = null;
		if (date != null) {
			start = date.atStartOfDay();
			end = start.plusDays(1);
		}

		Page<EmailNotificationResponse> emailGroupsPage = emailNotificationPort.loadEmailNotificationGroups(
			start,
			end,
			status,
			daysBeforePayment,
			email,
			pageable
		);

		return new EmailNotificationsResponse(
			emailGroupsPage.getContent(),
			emailGroupsPage.getNumber() + 1,
			emailGroupsPage.getTotalElements(),
			emailGroupsPage.getTotalPages()
		);
	}

	public List<EmailNotificationDetailResponse> getEmailNotificationDetails(
		String email,
		LocalDate paymentDate,
		Integer daysBeforePayment
	) {
		return emailNotificationPort.loadEmailNotificationDetails(email, paymentDate, daysBeforePayment);
	}
}
