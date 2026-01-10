package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.Subscription;

@Component
public class SubscriptionMapper {

	public SubscriptionJpaEntity toJpaEntity(
		Subscription subscription,
		MemberJpaEntity memberEntity
	) {
		return new SubscriptionJpaEntity(
			subscription.getName(),
			subscription.getType(),
			subscription.getLogoImageUrl(),
			subscription.getPlanUrl(),
			subscription.isSystemProvided(),
			memberEntity
		);
	}

	public Subscription toDomain(
		SubscriptionJpaEntity subscriptionEntity
	) {
		boolean systemProvided = subscriptionEntity.isSystemProvided();
		MemberJpaEntity memberEntity = subscriptionEntity.getMember();

		Long memberId = null;
		if (!systemProvided && memberEntity != null) {
			memberId = memberEntity.getId();
		}

		return Subscription.withId(
			subscriptionEntity.getId(),
			subscriptionEntity.getName(),
			subscriptionEntity.getType(),
			subscriptionEntity.getLogoImageUrl(),
			subscriptionEntity.getPlanUrl(),
			systemProvided,
			memberId
		);
	}
}
