package subport.admin.adapter.out.persistence.plan;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.application.plan.PlanPort;
import subport.common.exception.CustomException;
import subport.domain.plan.Plan;

@Component
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements PlanPort {

	private final SpringDataPlanRepository planRepository;

	@Override
	public Long save(Plan plan) {
		return planRepository.save(plan).getId();
	}

	@Override
	public List<Plan> loadPlans(Long subscriptionId) {
		return planRepository.findBySubscriptionId(subscriptionId);
	}

	@Override
	public Plan loadPlan(Long planId) {
		return planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(AdminErrorCode.PLAN_NOT_FOUND));
	}

	@Override
	public void delete(Plan plan) {
		planRepository.delete(plan);
	}
}
