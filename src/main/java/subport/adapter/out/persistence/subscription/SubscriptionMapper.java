package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.Subscription;

@Component
public class SubscriptionMapper {

	public SubscriptionJpaEntity toCustomSubscriptionJpaEntity(
		Subscription subscription,
		MemberJpaEntity memberEntity
	) {
		return SubscriptionJpaEntity.customSubscription(
			subscription.getName(),
			subscription.getType(),
			subscription.getLogoImageUrl(),
			subscription.getPlanUrl(),
			memberEntity
		);
	}

	public SubscriptionJpaEntity toSystemSubscriptionJpaEntity(
		Subscription subscription,
		MemberJpaEntity memberEntity
	) {
		return SubscriptionJpaEntity.systemSubscription(
			subscription.getName(),
			subscription.getType(),
			subscription.getLogoImageUrl(),
			subscription.getPlanUrl(),
			memberEntity
		);
	}
}
