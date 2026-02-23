package subport.api.adapter.out.persistence.plan;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.plan.port.out.DeletePlanPort;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.api.application.plan.port.out.SavePlanPort;
import subport.common.exception.CustomException;
import subport.domain.plan.Plan;

@Component
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements
	SavePlanPort,
	LoadPlanPort,
	DeletePlanPort {

	private final SpringDataPlanRepository planRepository;

	@Override
	public Long save(Plan plan) {
		return planRepository.save(plan).getId();
	}

	@Override
	public Plan loadPlan(Long planId) {
		return planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(ApiErrorCode.PLAN_NOT_FOUND));
	}

	@Override
	public List<Plan> loadPlans(Long memberId, Long subscriptionId) {
		return planRepository.findByMemberIdAndSubscriptionId(memberId, subscriptionId);
	}

	@Override
	public void delete(Plan plan) {
		planRepository.delete(plan);
	}

	@Override
	public void delete(Long subscriptionId) {
		planRepository.deleteBySubscriptionId(subscriptionId);
	}
}
