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
import subport.domain.member.Member;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateEmailNotificationsService implements CreateEmailNotificationsUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final SaveEmailNotificationsPort saveEmailNotificationsPort;

	@Override
	public void create(LocalDate currentDate) {
		List<EmailNotification> emailNotifications
			= loadMemberSubscriptionPort.loadMemberSubscriptionsForEmail(currentDate).stream()
			.map(memberSubscription -> {
				Member member = memberSubscription.getMember();
				Subscription subscription = memberSubscription.getSubscription();

				return new EmailNotification(
					memberSubscription.getId(),
					memberSubscription.getNextPaymentDate(),
					memberSubscription.getReminderDaysBefore(),
					member.getId(),
					member.getEmail(),
					subscription.getName(),
					subscription.getLogoImageUrl(),
					SendingStatus.PENDING,
					null,
					0
				);
			})
			.toList();

		saveEmailNotificationsPort.save(emailNotifications);
	}
}
