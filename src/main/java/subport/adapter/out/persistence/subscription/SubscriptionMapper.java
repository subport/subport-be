package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.domain.subscription.Subscription;

@Component
public class SubscriptionMapper {

	// 설계 잘못함
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
}
