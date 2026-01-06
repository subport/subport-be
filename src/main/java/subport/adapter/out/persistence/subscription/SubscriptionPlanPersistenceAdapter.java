package subport.adapter.out.persistence.subscription;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.application.subscription.port.out.SaveSubscriptionPlanPort;
import subport.domain.subscription.SubscriptionPlan;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanPersistenceAdapter implements SaveSubscriptionPlanPort {

	private final SpringDataSubscriptionPlanRepository subscriptionPlanRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SubscriptionPlanMapper subscriptionPlanMapper;

	@Override
	public void save(SubscriptionPlan subscriptionPlan) {
		MemberJpaEntity memberEntity = memberRepository.getReferenceById(subscriptionPlan.getMemberId());
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.getReferenceById(
			subscriptionPlan.getSubscriptionId());

		SubscriptionPlanJpaEntity subscriptionPlanEntity = subscriptionPlanMapper.toJpaEntity(
			subscriptionPlan,
			memberEntity,
			subscriptionEntity
		);

		subscriptionPlanRepository.save(subscriptionPlanEntity);
	}
}
