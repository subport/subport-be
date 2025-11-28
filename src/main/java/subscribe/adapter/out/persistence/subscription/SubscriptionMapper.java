package subscribe.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subscribe.adapter.out.persistence.member.MemberJpaEntity;
import subscribe.domain.subscription.Subscription;

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
