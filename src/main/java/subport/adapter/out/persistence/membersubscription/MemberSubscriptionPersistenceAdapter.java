package subport.adapter.out.persistence.membersubscription;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.adapter.out.persistence.subscription.SpringDataSubscriptionPlanRepository;
import subport.adapter.out.persistence.subscription.SpringDataSubscriptionRepository;
import subport.adapter.out.persistence.subscription.SubscriptionJpaEntity;
import subport.adapter.out.persistence.subscription.SubscriptionPlanJpaEntity;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Component
@RequiredArgsConstructor
public class MemberSubscriptionPersistenceAdapter implements SaveMemberSubscriptionPort {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SpringDataSubscriptionPlanRepository subscriptionPlanRepository;
	private final MemberSubscriptionMapper memberSubscriptionMapper;

	@Override
	public void save(MemberSubscription memberSubscription) {
		MemberJpaEntity memberEntity = memberRepository.getReferenceById(memberSubscription.getMemberId());
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.getReferenceById(
			memberSubscription.getSubscriptionId());
		SubscriptionPlanJpaEntity subscriptionPlanEntity = subscriptionPlanRepository.getReferenceById(
			memberSubscription.getSubscriptionPlanId());

		MemberSubscriptionJpaEntity memberSubscriptionEntity = memberSubscriptionMapper.toJpaEntity(
			memberSubscription,
			memberEntity,
			subscriptionEntity,
			subscriptionPlanEntity
		);

		memberSubscriptionRepository.save(memberSubscriptionEntity);
	}
}
