package subport.application.emailnotification.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.domain.emailnotification.EmailNotification;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

	private static final String LOGO_IMAGE_URL = "https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/axnklumwzgke/b/subpport-bucket/o/subport_logo_name.png";
	private static final String SINGLE_PAYMENT_REMINDER_SUBJECT = "[SUBPORT] %s 결제일이 %d일 남았어요.";
	private static final String MULTIPLE_PAYMENT_REMINDER_SUBJECT = "[SUBPORT] 곧 결제되는 구독 %d건, 미리 확인해 보세요.";

	private final JavaMailSender mailSender;
	private final EmailResultHandler emailResultHandler;
	private final TemplateEngine templateEngine;

	@Async
	public void sendAsync(
		List<EmailNotification> emailNotifications,
		boolean isRetry,
		LocalDateTime currentDateTime
	) {
		String recipientEmail = emailNotifications.get(0).getRecipientEmail();
		int subscriptionCount = emailNotifications.size();
		try {
			Context context = new Context();
			context.setVariable("emailNotifications", emailNotifications);
			context.setVariable("logoImageUrl2", LOGO_IMAGE_URL);

			String template = templateEngine.process("payment-reminder", context);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom("noreply@subport.site");
			helper.setTo(recipientEmail);
			helper.setText(template, true);

			String subject = String.format(MULTIPLE_PAYMENT_REMINDER_SUBJECT, subscriptionCount);
			if (subscriptionCount == 1) {
				EmailNotification emailNotification = emailNotifications.get(0);
				subject = String.format(
					SINGLE_PAYMENT_REMINDER_SUBJECT,
					emailNotification.getSubscriptionName(),
					emailNotification.getDaysBeforePayment()
				);
			}
			helper.setSubject(subject);

			mailSender.send(message);
			emailResultHandler.handleSuccess(
				emailNotifications,
				currentDateTime,
				isRetry
			);
		} catch (MessagingException e) {
			log.warn(
				"Send email failed for recipient {} (count: {}): {}",
				recipientEmail,
				subscriptionCount,
				e.getMessage()
			);
			emailResultHandler.handleFailure(
				emailNotifications,
				currentDateTime,
				isRetry
			);
		}
	}
}
