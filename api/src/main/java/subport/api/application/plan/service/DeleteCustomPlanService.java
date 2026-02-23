package subport.api.application.plan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.plan.port.in.DeleteCustomPlanUseCase;
import subport.api.application.plan.port.out.DeletePlanPort;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.common.exception.CustomException;
import subport.domain.plan.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCustomPlanService implements DeleteCustomPlanUseCase {

	private final LoadPlanPort loadPlanPort;
	private final DeletePlanPort deletePlanPort;

	@Override
	public void delete(Long memberId, Long planId) {
		Plan plan = loadPlanPort.loadPlan(planId);

		if (plan.isSystemProvided()) {
			throw new CustomException(ApiErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		deletePlanPort.delete(plan);
	}
}
