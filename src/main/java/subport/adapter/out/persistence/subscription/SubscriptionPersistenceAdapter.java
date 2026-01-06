package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.domain.subscription.Subscription;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SaveSubscriptionPort {

	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SubscriptionMapper subscriptionMapper;

	@Override
	public void save(Subscription subscription) {
		MemberJpaEntity memberEntity = null;
		if (subscription.getMemberId() != null) {
			memberEntity = memberRepository.getReferenceById(subscription.getMemberId());
		}

		SubscriptionJpaEntity subscriptionEntity = subscriptionMapper.toJpaEntity(
			subscription,
			memberEntity
		);

		subscriptionRepository.save(subscriptionEntity);
	}
}
