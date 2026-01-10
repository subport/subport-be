package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.UpdateCustomSubscriptionPlanRequest;
import subport.application.subscription.port.in.UpdateCustomSubscriptionPlanUseCase;
import subport.application.subscription.port.out.LoadSubscriptionPlanPort;
import subport.application.subscription.port.out.UpdateSubscriptionPlanPort;
import subport.domain.subscription.SubscriptionAmountUnit;
import subport.domain.subscription.SubscriptionPlan;

@Service
@RequiredArgsConstructor
public class UpdateCustomSubscriptionPlanService implements UpdateCustomSubscriptionPlanUseCase {

	private final LoadSubscriptionPlanPort loadSubscriptionPlanPort;
	private final UpdateSubscriptionPlanPort updateSubscriptionPlanPort;

	@Transactional
	@Override
	public void update(
		Long memberId,
		UpdateCustomSubscriptionPlanRequest request,
		Long subscriptionPlanId
	) {
		SubscriptionPlan subscriptionPlan = loadSubscriptionPlanPort.load(subscriptionPlanId);

		if (subscriptionPlan.isSystemProvided()) {
			throw new CustomException(ErrorCode.SYSTEM_PLAN_WRITE_FORBIDDEN);
		}

		if (!subscriptionPlan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_WRITE_FORBIDDEN);
		}

		subscriptionPlan.update(
			request.planName(),
			request.amount(),
			SubscriptionAmountUnit.fromString(request.amountUnit()),
			request.durationMonths()
		);

		updateSubscriptionPlanPort.update(subscriptionPlan);
	}
}
