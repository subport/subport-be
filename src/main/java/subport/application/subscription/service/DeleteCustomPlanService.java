package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.DeleteCustomPlanUseCase;
import subport.application.subscription.port.out.DeletePlanPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.subscription.Plan;

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
			throw new CustomException(ErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		deletePlanPort.delete(plan);
	}
}
