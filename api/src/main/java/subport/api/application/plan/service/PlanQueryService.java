package subport.api.application.plan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.plan.port.in.PlanQueryUseCase;
import subport.api.application.plan.port.in.dto.GetPlanResponse;
import subport.api.application.plan.port.in.dto.GetPlansResponse;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.common.exception.CustomException;
import subport.domain.plan.Plan;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanQueryService implements PlanQueryUseCase {

	private final LoadPlanPort loadPlanPort;

	@Override
	public GetPlanResponse getPlan(Long memberId, Long planId) {
		Plan plan = loadPlanPort.loadPlan(planId);

		if (!plan.isSystemProvided() && !plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.PLAN_READ_FORBIDDEN);
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
