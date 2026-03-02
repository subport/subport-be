package subport.batch.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import subport.batch.persistence.SpringDataEmailNotificationRepository;
import subport.batch.persistence.SpringDataMemberSubscriptionRepository;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;
import subport.domain.member.Member;
import subport.domain.plan.Plan;
import subport.domain.subscription.Subscription;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

	private final SpringDataEmailNotificationRepository emailNotificationRepository;
	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;
	private final EmailSender emailSender;

	@Transactional
	public void create(LocalDate today) {
		log.info("이메일 알림 생성 시작");

		List<EmailNotification> emailNotifications =
			memberSubscriptionRepository.findActiveByPaymentReminderDate(today).stream()
				.map(memberSubscription -> {
					Member member = memberSubscription.getMember();
					Subscription subscription = memberSubscription.getSubscription();
					Plan plan = memberSubscription.getPlan();

					BigDecimal amount = plan.getAmount();
					if (memberSubscription.isDutchPay()) {
						amount = memberSubscription.getDutchPayAmount();
					}

					return new EmailNotification(
						memberSubscription.getId(),
						memberSubscription.getNextPaymentDate(),
						member.getReminderDaysBefore(),
						member.getId(),
						member.getEmail(),
						subscription.getName(),
						subscription.getLogoImageUrl(),
						new DecimalFormat("#,###").format(amount),
						plan.getAmountUnit().getDisplayName(),
						plan.getDurationMonths(),
						SendingStatus.PENDING,
						null,
						0
					);
				})
				.toList();

		emailNotificationRepository.saveAll(emailNotifications);

		log.info("이메일 알림 생성 완료 - 생성 건수: {}", emailNotifications.size());
	}

	@Transactional(readOnly = true)
	public void send(LocalDateTime now) {
		List<EmailNotification> emailNotifications = getEmailNotifications(now, SendingStatus.PENDING);

		log.info("이메일 발송 시작 - 대상 건수: {}", emailNotifications.size());

		Map<String, List<EmailNotification>> groupedEmailNotifications = emailNotifications.stream()
			.collect(Collectors.groupingBy(EmailNotification::getRecipientEmail));

		for (Map.Entry<String, List<EmailNotification>> entry : groupedEmailNotifications.entrySet()) {
			emailSender.sendAsync(entry.getValue(), false, now);
		}

		log.info("이메일 발송 요청 완료 - 수신자 수: {}", groupedEmailNotifications.size());
	}

	@Transactional(readOnly = true)
	public void retry(LocalDateTime now) {
		List<EmailNotification> emailNotifications = getEmailNotifications(now, SendingStatus.FAILED);

		log.info("이메일 재발송 시작 - 대상 건수: {}", emailNotifications.size());

		Map<String, List<EmailNotification>> groupedEmailNotifications = emailNotifications.stream()
			.collect(Collectors.groupingBy(EmailNotification::getRecipientEmail));

		for (Map.Entry<String, List<EmailNotification>> entry : groupedEmailNotifications.entrySet()) {
			emailSender.sendAsync(entry.getValue(), true, now);
		}

		log.info("이메일 재발송 요청 완료 - 수신자 수: {}", groupedEmailNotifications.size());
	}

	private List<EmailNotification> getEmailNotifications(LocalDateTime now, SendingStatus status) {
		return emailNotificationRepository.findByStatusAndCreatedAtBetween(
			now.toLocalDate().atStartOfDay(),
			now.toLocalDate().plusDays(1).atStartOfDay(),
			status
		);
	}
}
