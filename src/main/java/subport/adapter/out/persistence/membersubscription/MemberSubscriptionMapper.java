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
		PlanJpaEntity subscriptionPlanEntity
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
			subscriptionEntity,
			subscriptionPlanEntity
		);
	}

	public MemberSubscription toDomain(MemberSubscriptionJpaEntity memberSubscriptionEntity) {
		return MemberSubscription.withId(
			memberSubscriptionEntity.getId(),
			memberSubscriptionEntity.getStartDate(),
			memberSubscriptionEntity.getReminderDaysBeforeEnd(),
			memberSubscriptionEntity.getMemo(),
			memberSubscriptionEntity.isDutchPay(),
			memberSubscriptionEntity.getDutchPayAmount(),
			memberSubscriptionEntity.getTerminationDate(),
			memberSubscriptionEntity.getNextPaymentDate(),
			memberSubscriptionEntity.getMember().getId(),
			memberSubscriptionEntity.getSubscription().getId(),
			memberSubscriptionEntity.getSubscriptionPlan().getId()
		);
	}
}
