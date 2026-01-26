package subport.application.emailnotification.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.emailnotification.port.in.CreateEmailNotificationsUseCase;
import subport.application.emailnotification.port.out.SaveEmailNotificationsPort;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.domain.emailnotification.EmailNotification;
import subport.domain.emailnotification.SendingStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateEmailNotificationsService implements CreateEmailNotificationsUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final SaveEmailNotificationsPort saveEmailNotificationsPort;

	@Override
	public void create(LocalDate currentDate) {
		List<EmailNotification> emailNotifications = loadMemberSubscriptionPort.loadForEmail(currentDate).stream()
			.map(memberSubscription -> EmailNotification.withoutId(
				memberSubscription.memberSubscriptionId(),
				memberSubscription.paymentDate(),
				memberSubscription.daysBeforePayment(),
				memberSubscription.memberId(),
				memberSubscription.memberEmail(),
				memberSubscription.subscriptionName(),
				memberSubscription.subscriptionLogoImageUrl(),
				SendingStatus.PENDING,
				null,
				0
			))
			.toList();

		saveEmailNotificationsPort.save(emailNotifications);
	}
}
