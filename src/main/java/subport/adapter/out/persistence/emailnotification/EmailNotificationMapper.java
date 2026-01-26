package subport.adapter.out.persistence.emailnotification;

import org.springframework.stereotype.Component;

import subport.domain.emailnotification.EmailNotification;

@Component
public class EmailNotificationMapper {

	public EmailNotificationJpaEntity toJpaEntity(EmailNotification emailNotification) {
		return new EmailNotificationJpaEntity(
			emailNotification.getMemberSubscriptionId(),
			emailNotification.getPaymentDate(),
			emailNotification.getDaysBeforePayment(),
			emailNotification.getMemberId(),
			emailNotification.getRecipientEmail(),
			emailNotification.getSubscriptionName(),
			emailNotification.getSubscriptionLogoImageUrl(),
			emailNotification.getStatus(),
			emailNotification.getSentAt(),
			emailNotification.getRetryCount()
		);
	}
}
