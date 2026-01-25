package subport.adapter.out.persistence.membersubscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.subscription.PlanJpaEntity;
import subport.adapter.out.persistence.subscription.SubscriptionJpaEntity;
import subport.domain.membersubscription.MemberSubscription;

@Component
public class MemberSubscriptionMapper {

	public MemberSubscriptionJpaEntity toJpaEntity(
		MemberSubscription memberSubscription,
		MemberJpaEntity memberEntity,
		SubscriptionJpaEntity subscriptionEntity,
		PlanJpaEntity planEntity
	) {
		return new MemberSubscriptionJpaEntity(
			memberSubscription.getStartDate(),
			memberSubscription.getReminderDaysBefore(),
			memberSubscription.getReminderDate(),
			memberSubscription.getMemo(),
			memberSubscription.isDutchPay(),
			memberSubscription.getDutchPayAmount(),
			memberSubscription.getExchangeRate(),
			memberSubscription.getExchangeRateDate(),
			memberSubscription.isActive(),
			memberSubscription.getLastPaymentDate(),
			memberSubscription.getNextPaymentDate(),
			memberEntity,
			subscriptionEntity,
			planEntity
		);
	}

	public MemberSubscription toDomain(MemberSubscriptionJpaEntity memberSubscriptionEntity) {
		return MemberSubscription.withId(
			memberSubscriptionEntity.getId(),
			memberSubscriptionEntity.getStartDate(),
			memberSubscriptionEntity.getReminderDaysBefore(),
			memberSubscriptionEntity.getMemo(),
			memberSubscriptionEntity.isDutchPay(),
			memberSubscriptionEntity.getDutchPayAmount(),
			memberSubscriptionEntity.getExchangeRate(),
			memberSubscriptionEntity.getExchangeRateDate(),
			memberSubscriptionEntity.isActive(),
			memberSubscriptionEntity.getLastPaymentDate(),
			memberSubscriptionEntity.getNextPaymentDate(),
			memberSubscriptionEntity.getMember().getId(),
			memberSubscriptionEntity.getSubscription().getId(),
			memberSubscriptionEntity.getPlan().getId()
		);
	}
}
