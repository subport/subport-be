package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.subscription.port.in.RegisterCustomSubscriptionPlanRequest;
import subport.application.subscription.port.in.RegisterCustomSubscriptionPlanUseCase;
import subport.application.subscription.port.out.RegisterCustomSubscriptionPlanResponse;
import subport.application.subscription.port.out.SaveSubscriptionPlanPort;
import subport.domain.subscription.SubscriptionAmountUnit;
import subport.domain.subscription.SubscriptionPlan;

@Service
@RequiredArgsConstructor
public class RegisterCustomSubscriptionPlanService implements RegisterCustomSubscriptionPlanUseCase {

	private final SaveSubscriptionPlanPort saveSubscriptionPlanPort;

	@Transactional
	@Override
	public RegisterCustomSubscriptionPlanResponse register(
		Long memberId,
		RegisterCustomSubscriptionPlanRequest request,
		Long subscriptionId
	) {

		SubscriptionPlan subscriptionPlan = SubscriptionPlan.withoutId(
			request.planName(),
			request.amount(),
			SubscriptionAmountUnit.fromString(request.amountUnit()),
			request.durationMonths(),
			false,
			memberId,
			subscriptionId
		);

		return new RegisterCustomSubscriptionPlanResponse(saveSubscriptionPlanPort.save(subscriptionPlan));
	}
}
