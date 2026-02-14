package subport.domain.emailnotification;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import subport.adapter.out.persistence.BaseTimeEntity;

@Entity
@Table(name = "email_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailNotification extends BaseTimeEntity {

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

	private String amount;

	private String amountUnit;

	private int planDurationMonths;

	@Enumerated(value = EnumType.STRING)
	private SendingStatus status;

	private LocalDateTime sentAt;

	private int retryCount;

	public EmailNotification(
		Long memberSubscriptionId,
		LocalDate paymentDate,
		Integer daysBeforePayment,
		Long memberId,
		String recipientEmail,
		String subscriptionName,
		String subscriptionLogoImageUrl,
		String amount,
		String amountUnit,
		int planDurationMonths,
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
		this.amount = amount;
		this.amountUnit = amountUnit;
		this.planDurationMonths = planDurationMonths;
		this.status = status;
		this.sentAt = sentAt;
		this.retryCount = retryCount;
	}

	public void markSent(LocalDateTime sentAt) {
		this.status = SendingStatus.SENT;
		this.sentAt = sentAt;
	}

	public void markFailed(LocalDateTime sentAt) {
		this.status = SendingStatus.FAILED;
		this.sentAt = sentAt;
	}

	public void increaseRetryCount() {
		retryCount++;
	}
}
