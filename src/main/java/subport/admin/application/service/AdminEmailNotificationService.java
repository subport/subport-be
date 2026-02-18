package subport.admin.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminEmailNotificationResponse;
import subport.admin.application.dto.AdminEmailNotificationsResponse;
import subport.admin.application.dto.AdminSubscriptionInNotificationResponse;
import subport.admin.application.port.AdminEmailNotificationPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEmailNotificationService {

	private final AdminEmailNotificationPort adminEmailNotificationPort;

	public AdminEmailNotificationsResponse searchEmailNotifications(
		LocalDate date,
		SendingStatus status,
		Integer daysBeforePayment,
		String email,
		Pageable pageable
	) {
		Page<EmailNotification> emailNotifications = adminEmailNotificationPort.searchEmailNotifications(
			date,
			status,
			daysBeforePayment,
			email,
			pageable
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
					notification.getSentAt(),
					subscriptions
				);
			})
			.toList();

		return new AdminEmailNotificationsResponse(
			items,
			emailNotifications.getNumber() + 1,
			emailNotifications.getTotalElements(),
			emailNotifications.getTotalPages()
		);
	}
}
