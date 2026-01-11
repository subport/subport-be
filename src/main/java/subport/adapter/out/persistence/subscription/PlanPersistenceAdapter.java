package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.adapter.out.persistence.member.MemberJpaEntity;
import subport.adapter.out.persistence.member.SpringDataMemberRepository;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeletePlanPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.SavePlanPort;
import subport.application.subscription.port.out.UpdatePlanPort;
import subport.domain.subscription.Plan;

@Component
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements
	SavePlanPort,
	LoadPlanPort,
	UpdatePlanPort,
	DeletePlanPort {

	private final SpringDataPlanRepository planRepository;
	private final SpringDataMemberRepository memberRepository;
	private final SpringDataSubscriptionRepository subscriptionRepository;
	private final PlanMapper planMapper;

	@Override
	public Long save(Plan plan) {
		MemberJpaEntity memberEntity = memberRepository.getReferenceById(plan.getMemberId());
		SubscriptionJpaEntity subscriptionEntity = subscriptionRepository.getReferenceById(
			plan.getSubscriptionId());

		PlanJpaEntity planEntity = planMapper.toJpaEntity(
			plan,
			memberEntity,
			subscriptionEntity
		);

		return planRepository.save(planEntity).getId();
	}

	@Override
	public Plan load(Long planId) {
		PlanJpaEntity planEntity = planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

		return planMapper.toDomain(planEntity);
	}

	@Override
	public List<Plan> loadByMemberIdAndSubscriptionId(Long memberId, Long subscriptionId) {
		return planRepository.findByIdAccessibleToMember(memberId, subscriptionId).stream()
			.map(planMapper::toDomain)
			.toList();
	}

	@Override
	public void update(Plan plan) {
		PlanJpaEntity planEntity = planRepository.findById(plan.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

		planEntity.apply(plan);
	}

	@Override
	public void deleteById(Long planId) {
		planRepository.deleteById(planId);
	}

	@Override
	public void deleteBySubscriptionId(Long subscriptionId) {
		planRepository.deleteAllBySubscriptionId(subscriptionId);
	}
}
