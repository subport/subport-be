package subport.domain.emailnotification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class EmailNotification {

	private final Long id;

	private final Long memberSubscriptionId;

	private final LocalDate paymentDate;

	private final Integer daysBeforePayment;

	private final Long memberId;

	private final String recipientEmail;

	private final String subscriptionName;

	private final String subscriptionLogoImageUrl;

	private final SendingStatus status;

	private final LocalDateTime sentAt;

	private final int retryCount;

	public EmailNotification(
		Long id,
		Long memberSubscriptionId,
		LocalDate paymentDate,
		Integer daysBeforePayment,
		Long memberId,
		String recipientEmail,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		SendingStatus status,
		LocalDateTime sentAt,
		int retryCount
	) {
		this.id = id;
		this.memberSubscriptionId = memberSubscriptionId;
		this.paymentDate = paymentDate;
		this.daysBeforePayment = daysBeforePayment;
		this.memberId = memberId;
		this.recipientEmail = recipientEmail;
		this.subscriptionName = subscriptionName;
		this.subscriptionLogoImageUrl = subscriptionLogoImageUrl;
		this.status = status;
		this.sentAt = sentAt;
		this.retryCount = retryCount;
	}

	public static EmailNotification withId(
		Long id,
		Long memberSubscriptionId,
		LocalDate paymentDate,
		Integer daysBeforePayment,
		Long memberId,
		String recipientEmail,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		SendingStatus status,
		LocalDateTime sentAt,
		int retryCount
	) {
		return new EmailNotification(
			id,
			memberSubscriptionId,
			paymentDate,
			daysBeforePayment,
			memberId,
			recipientEmail,
			subscriptionName,
			subscriptionLogoImageUrl,
			status,
			sentAt,
			retryCount
		);
	}

	public static EmailNotification withoutId(
		Long memberSubscriptionId,
		LocalDate paymentDate,
		Integer daysBeforePayment,
		Long memberId,
		String recipientEmail,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		SendingStatus status,
		LocalDateTime sentAt,
		int retryCount
	) {
		return new EmailNotification(
			null,
			memberSubscriptionId,
			paymentDate,
			daysBeforePayment,
			memberId,
			recipientEmail,
			subscriptionName,
			subscriptionLogoImageUrl,
			status,
			sentAt,
			retryCount
		);
	}
}
