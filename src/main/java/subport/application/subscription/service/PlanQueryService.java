package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.PlanQueryUseCase;
import subport.application.subscription.port.in.dto.GetPlanResponse;
import subport.application.subscription.port.in.dto.GetPlansResponse;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.subscription.Plan;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanQueryService implements PlanQueryUseCase {

	private final LoadPlanPort loadPlanPort;

	@Override
	public GetPlanResponse getPlan(Long memberId, Long planId) {
		Plan plan = loadPlanPort.loadPlan(planId);

		if (!plan.isSystemProvided() && !plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_READ_FORBIDDEN);
		}

		return GetPlanResponse.from(plan);
	}

	@Override
	public GetPlansResponse getPlans(Long memberId, Long subscriptionId) {
		return GetPlansResponse.from(
			loadPlanPort.loadPlans(memberId, subscriptionId)
		);
	}
}
