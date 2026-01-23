package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeleteSubscriptionPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.application.subscription.port.out.UpdateSubscriptionPort;
import subport.domain.subscription.Subscription;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements
	SaveSubscriptionPort,
	LoadSubscriptionPort,
	UpdateSubscriptionPort,
	DeleteSubscriptionPort {

	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SubscriptionMapper subscriptionMapper;

	@Override
	public Long save(Subscription subscription) {
		MemberJpaEntity memberEntity = null;
		if (subscription.getMemberId() != null) {
			memberEntity = memberRepository.getReferenceById(subscription.getMemberId());
		}

		SubscriptionJpaEntity subscriptionEntity = subscriptionMapper.toJpaEntity(
			subscription,
			memberEntity
		);

		return subscriptionRepository.save(subscriptionEntity).getId();
	}

	@Override
	public Subscription loadSubscription(Long subscriptionId) {
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

		return subscriptionMapper.toDomain(subscriptionEntity);
	}

	@Override
	public List<Subscription> searchSubscriptions(Long memberId, String name) {
		return subscriptionRepository.findByMemberIdAndNameContaining(memberId, name).stream()
			.map(subscriptionMapper::toDomain)
			.toList();
	}

	@Override
	public void update(Subscription subscription) {
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.findById(subscription.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

		subscriptionEntity.apply(subscription);
	}

	@Override
	public void delete(Long subscriptionId) {
		subscriptionRepository.deleteById(subscriptionId);
	}
}
