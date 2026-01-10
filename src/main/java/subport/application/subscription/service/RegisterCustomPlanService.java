package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.subscription.port.in.RegisterCustomPlanRequest;
import subport.application.subscription.port.in.RegisterCustomPlanUseCase;
import subport.application.subscription.port.out.RegisterCustomPlanResponse;
import subport.application.subscription.port.out.SavePlanPort;
import subport.domain.subscription.SubscriptionAmountUnit;
import subport.domain.subscription.Plan;

@Service
@RequiredArgsConstructor
public class RegisterCustomPlanService implements RegisterCustomPlanUseCase {

	private final SavePlanPort savePlanPort;

	@Transactional
	@Override
	public RegisterCustomPlanResponse register(
		Long memberId,
		RegisterCustomPlanRequest request,
		Long subscriptionId
	) {

		Plan plan = Plan.withoutId(
			request.name(),
			request.amount(),
			SubscriptionAmountUnit.fromString(request.amountUnit()),
			request.durationMonths(),
			false,
			memberId,
			subscriptionId
		);

		return new RegisterCustomPlanResponse(savePlanPort.save(plan));
	}
}
