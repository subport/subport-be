package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.DeleteCustomPlanUseCase;
import subport.application.subscription.port.out.DeleteSubscriptionPlanPort;
import subport.application.subscription.port.out.LoadSubscriptionPlanPort;
import subport.domain.subscription.SubscriptionPlan;

@Service
@RequiredArgsConstructor
public class DeleteCustomPlanService implements DeleteCustomPlanUseCase {

	private final LoadSubscriptionPlanPort loadSubscriptionPlanPort;
	private final DeleteSubscriptionPlanPort deleteSubscriptionPlanPort;

	@Transactional
	@Override
	public void delete(Long memberId, Long planId) {
		SubscriptionPlan subscriptionPlan = loadSubscriptionPlanPort.load(planId);

		if (subscriptionPlan.isSystemProvided()) {
			throw new CustomException(ErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!subscriptionPlan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		deleteSubscriptionPlanPort.deleteById(planId);
	}
}
