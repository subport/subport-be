package subscribe.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subscribe.adapter.out.persistence.member.MemberJpaEntity;
import subscribe.adapter.out.persistence.member.SpringDataMemberRepository;
import subscribe.application.subscribe.port.out.SaveSubscriptionPort;
import subscribe.domain.subscription.Subscription;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SaveSubscriptionPort {

	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SpringDataSubscriptionTypeRepository subscriptionTypeRepository;
	private final SpringDataSubscriptionPlanRepository subscriptionPlanRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SubscriptionMapper subscriptionMapper;

	@Override
	public void saveSubscription(Subscription subscription) {
		SubscriptionTypeJpaEntity typeEntity = subscriptionTypeRepository.getReferenceById(subscription.getTypeId());
		SubscriptionPlanJpaEntity planEntity = subscriptionPlanRepository.getReferenceById(subscription.getPlanId());
		MemberJpaEntity memberEntity = memberRepository.getReferenceById(subscription.getMemberId());

		SubscriptionJpaEntity subscriptionEntity = subscriptionMapper.toJpaEntity(
			subscription,
			typeEntity,
			planEntity,
			memberEntity
		);

		subscriptionRepository.save(subscriptionEntity);
	}
}
