package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.Subscription;

@Component
public class SubscriptionMapper {

	public SubscriptionJpaEntity toJpaEntity(
		Subscription subscription,
		SubscriptionTypeJpaEntity subscriptionTypeEntity,
		SubscriptionPlanJpaEntity subscriptionPlanEntity,
		MemberJpaEntity memberEntity
	) {
		return new SubscriptionJpaEntity(
			subscription.getName(),
			subscriptionTypeEntity,
			subscriptionPlanEntity,
			subscription.getHeadCount(),
			subscription.getStartAt(),
			subscription.getEndAt(),
			subscription.getMemo(),
			memberEntity
		);
	}
}
