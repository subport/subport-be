package subport.admin.application.emailnotification;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.emailnotification.dto.AdminEmailNotificationResponse;
import subport.admin.application.emailnotification.dto.AdminEmailNotificationsResponse;
import subport.admin.application.emailnotification.dto.AdminSubscriptionInNotificationResponse;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailNotificationQueryService {

	private final EmailNotificationPort emailNotificationPort;

	public AdminEmailNotificationsResponse searchEmailNotifications(
		LocalDate date,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	) {
		Page<String> emailsPage = emailNotificationPort.searchDistinctRecipientEmails(
			date,
			status,
			daysBeforePayment,
			email,
			pageable
		);

		List<EmailNotification> emailNotifications = emailNotificationPort.searchEmailNotifications(
			emailsPage.getContent(),
			date,
			status,
			daysBeforePayment,
			email
		);

		Map<String, List<EmailNotification>> grouped = emailNotifications.stream()
			.collect(Collectors.groupingBy(
				EmailNotification::getRecipientEmail));

		List<AdminEmailNotificationResponse> items = grouped.entrySet().stream()
			.map(entry -> {
				String recipientEmail = entry.getKey();
				List<EmailNotification> notifications = entry.getValue();

				List<AdminSubscriptionInNotificationResponse> subscriptions = notifications.stream()
					.map(notification ->
						new AdminSubscriptionInNotificationResponse(
							notification.getSubscriptionLogoImageUrl(),
							notification.getSubscriptionName(),
							notification.getAmount(),
							notification.getAmountUnit()
						))
					.toList();

				EmailNotification notification = notifications.get(0);

				return new AdminEmailNotificationResponse(
					recipientEmail,
					notification.getPaymentDate(),
					notification.getDaysBeforePayment(),
					notification.getStatus(),
					notification.getRetryCount(),
					notification.getSentAt(),
					subscriptions
				);
			})
			.sorted(Comparator.comparing(AdminEmailNotificationResponse::sentAt).reversed())
			.toList();

		return new AdminEmailNotificationsResponse(
			items,
			emailsPage.getNumber() + 1,
			emailsPage.getTotalElements(),
			emailsPage.getTotalPages()
		);
	}
}
