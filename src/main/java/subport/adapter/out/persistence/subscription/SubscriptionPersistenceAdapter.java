package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.application.subscription.port.out.UpdateSubscriptionPort;
import subport.domain.subscription.Subscription;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements
	SaveSubscriptionPort,
	LoadSubscriptionPort,
	UpdateSubscriptionPort {

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

	@Override
	public Subscription load(Long id) {
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

		return subscriptionMapper.toDomain(subscriptionEntity);
	}

	@Override
	public void update(Subscription subscription) {
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.findById(subscription.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

		subscriptionEntity.apply(subscription);
	}
}
