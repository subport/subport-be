package subport.application.membersubscription.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Service
@RequiredArgsConstructor
public class RegisterMemberSubscriptionService implements RegisterMemberSubscriptionUseCase {

	private final SaveMemberSubscriptionPort saveMemberSubscriptionPort;

	@Transactional
	@Override
	public void register(Long memberId, RegisterMemberSubscriptionRequest request) {
		LocalDate nextPaymentDate = request.startDate().plusMonths(1);

		MemberSubscription memberSubscription = MemberSubscription.withoutId(
			request.startDate(),
			request.reminderDaysBeforeEnd(),
			request.memo(),
			request.dutchPay(),
			request.dutchPayAmount(),
			null,
			nextPaymentDate,
			memberId,
			request.subscriptionId(),
			request.subscriptionPlanId()
		);

		saveMemberSubscriptionPort.save(memberSubscription);
	}
}
