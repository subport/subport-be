package subport.adapter.out.persistence.membersubscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.subscription.SubscriptionJpaEntity;
import subport.domain.membersubscription.MemberSubscription;

@Component
public class MemberSubscriptionMapper {

	public MemberSubscriptionJpaEntity toJpaEntity(
		MemberSubscription memberSubscription,
		MemberJpaEntity memberEntity,
		SubscriptionJpaEntity subscriptionEntity
	) {
		return new MemberSubscriptionJpaEntity(
			memberSubscription.getStartDate(),
			memberSubscription.getReminderDaysBeforeEnd(),
			memberSubscription.getMemo(),
			memberSubscription.isDutchPay(),
			memberSubscription.getDutchPayAmount(),
			memberSubscription.isActive(),
			memberSubscription.getTerminationDate(),
			memberSubscription.getNextPaymentDate(),
			memberEntity,
			subscriptionEntity
		);
	}
}
