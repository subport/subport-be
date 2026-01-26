package subport.adapter.out.persistence.emailnotification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;
import subport.domain.emailnotification.SendingStatus;

@Entity
@Table(name = "email_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailNotificationJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberSubscriptionId;

	private LocalDate paymentDate;

	private Integer daysBeforePayment;

	private Long memberId;

	private String recipientEmail;

	private String subscriptionName;

	private String subscriptionLogoImageUrl;

	@Enumerated(value = EnumType.STRING)
	private SendingStatus status;

	private LocalDateTime sentAt;

	private int retryCount;

	public EmailNotificationJpaEntity(
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
}
