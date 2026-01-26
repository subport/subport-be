package subport.application.emailnotification.service;

import java.time.LocalDateTime;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.domain.emailnotification.EmailNotification;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSender {

	private final JavaMailSender mailSender;
	private final EmailResultHandler emailResultHandler;

	@Async
	public void sendAsync(EmailNotification emailNotification, boolean isRetry) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setFrom("noreply@subport.site");
			helper.setTo(emailNotification.getRecipientEmail());
			helper.setSubject("테스트 메일");

			helper.setText("""
				<html>
				<body>
				    <h2>이메일 발송 테스트입니다.</h2>
				</body>
				</html>
				""", true);

			mailSender.send(message);
			emailResultHandler.handleSuccess(
				emailNotification,
				LocalDateTime.now(),
				isRetry
			);
		} catch (MessagingException e) {
			log.warn(
				"Send email failed for memberSubscriptionId {}: {}",
				emailNotification.getMemberSubscriptionId(),
				e.getMessage()
			);
			emailResultHandler.handleFailure(emailNotification, isRetry);
		}
	}
}
