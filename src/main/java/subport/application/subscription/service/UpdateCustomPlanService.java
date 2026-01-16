package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.dto.UpdateCustomPlanRequest;
import subport.application.subscription.port.in.UpdateCustomPlanUseCase;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.UpdatePlanPort;
import subport.domain.subscription.Plan;
import subport.domain.subscription.SubscriptionAmountUnit;

@Service
@RequiredArgsConstructor
public class UpdateCustomPlanService implements UpdateCustomPlanUseCase {

	private final LoadPlanPort loadPlanPort;
	private final UpdatePlanPort updatePlanPort;

	@Transactional
	@Override
	public void update(
		Long memberId,
		UpdateCustomPlanRequest request,
		Long planId
	) {
		Plan plan = loadPlanPort.load(planId);

		if (plan.isSystemProvided()) {
			throw new CustomException(ErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!plan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		plan.update(
			request.name(),
			request.amount(),
			SubscriptionAmountUnit.fromString(request.amountUnit()),
			request.durationMonths()
		);

		updatePlanPort.update(plan);
	}
}
