package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.PlanQueryUseCase;
import subport.application.subscription.port.in.UpdateCustomPlanUseCase;
import subport.application.subscription.port.in.dto.GetPlanResponse;
import subport.application.subscription.port.in.dto.UpdateCustomPlanRequest;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;

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
			throw new CustomException(ErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		plan.update(
			request.name(),
			request.amount(),
			AmountUnit.fromString(request.amountUnit()),
			request.durationMonths()
		);

		return planQueryUseCase.getPlan(memberId, planId);
	}
}
