package subport.api.application.plan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.plan.port.in.PlanQueryUseCase;
import subport.api.application.plan.port.in.UpdateCustomPlanUseCase;
import subport.api.application.plan.port.in.dto.GetPlanResponse;
import subport.api.application.plan.port.in.dto.UpdateCustomPlanRequest;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.common.exception.CustomException;
import subport.domain.plan.AmountUnit;
import subport.domain.plan.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCustomPlanService implements UpdateCustomPlanUseCase {

	private final LoadPlanPort loadPlanPort;
	private final PlanQueryUseCase planQueryUseCase;

	@Override
	public GetPlanResponse update(
		Long memberId,
		UpdateCustomPlanRequest request,
		Long planId
	) {
		Plan plan = loadPlanPort.loadPlan(planId);

		if (plan.isSystemProvided()) {
			throw new CustomException(ApiErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		plan.update(
			request.name(),
			request.amount(),
			AmountUnit.valueOf(request.amountUnit()),
			request.durationMonths()
		);

		return planQueryUseCase.getPlan(memberId, planId);
	}
}
