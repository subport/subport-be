package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeleteSubscriptionPlanPort;
import subport.application.subscription.port.out.LoadSubscriptionPlanPort;
import subport.application.subscription.port.out.SaveSubscriptionPlanPort;
import subport.application.subscription.port.out.UpdateSubscriptionPlanPort;
import subport.domain.subscription.SubscriptionPlan;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanPersistenceAdapter implements
	SaveSubscriptionPlanPort,
	LoadSubscriptionPlanPort,
	UpdateSubscriptionPlanPort,
	DeleteSubscriptionPlanPort {

	private final SpringDataSubscriptionPlanRepository subscriptionPlanRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final SubscriptionPlanMapper subscriptionPlanMapper;

	@Override
	public Long save(SubscriptionPlan subscriptionPlan) {
		MemberJpaEntity memberEntity = memberRepository.getReferenceById(subscriptionPlan.getMemberId());
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.getReferenceById(
			subscriptionPlan.getSubscriptionId());

		SubscriptionPlanJpaEntity subscriptionPlanEntity = subscriptionPlanMapper.toJpaEntity(
			subscriptionPlan,
			memberEntity,
			subscriptionEntity
		);

		return subscriptionPlanRepository.save(subscriptionPlanEntity).getId();
	}

	@Override
	public SubscriptionPlan load(Long subscriptionPlanId) {
		SubscriptionPlanJpaEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(subscriptionPlanId)
			.orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

		return subscriptionPlanMapper.toDomain(subscriptionPlanEntity);
	}

	@Override
	public List<SubscriptionPlan> loadByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId) {
		return subscriptionPlanRepository.findByIdAccessibleToMember(memberId, subscriptionId).stream()
			.map(subscriptionPlanMapper::toDomain)
			.toList();
	}

	@Override
	public void update(SubscriptionPlan subscriptionPlan) {
		SubscriptionPlanJpaEntity subscriptionPlanEntity = subscriptionPlanRepository.findById(subscriptionPlan.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

		subscriptionPlanEntity.apply(subscriptionPlan);
	}

	@Override
	public void deleteById(Long planId) {
		subscriptionPlanRepository.deleteById(planId);
	}

	@Override
	public void deleteBySubscriptionId(Long subscriptionId) {
		subscriptionPlanRepository.deleteAllBySubscriptionId(subscriptionId);
	}
}
