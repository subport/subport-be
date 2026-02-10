package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminPlanPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeletePlanPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.SavePlanPort;
import subport.domain.subscription.Plan;

@Component
@RequiredArgsConstructor
public class PlanPersistenceAdapter implements
	SavePlanPort,
	LoadPlanPort,
	DeletePlanPort,
	AdminPlanPort {

	private final SpringDataPlanRepository planRepository;

	@Override
	public Long save(Plan plan) {
		return planRepository.save(plan).getId();
	}

	@Override
	public Plan loadPlan(Long planId) {
		return planRepository.findById(planId)
			.orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));
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

	@Override
	public List<Plan> loadPlans(Long subscriptionId) {
		return planRepository.findBySubscriptionId(subscriptionId);
	}
}
