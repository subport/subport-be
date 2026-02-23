package subport.api.application.plan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.api.application.plan.port.in.RegisterCustomPlanUseCase;
import subport.api.application.plan.port.in.dto.RegisterCustomPlanRequest;
import subport.api.application.plan.port.in.dto.RegisterCustomPlanResponse;
import subport.api.application.plan.port.out.SavePlanPort;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.domain.member.Member;
import subport.domain.plan.AmountUnit;
import subport.domain.plan.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCustomPlanService implements RegisterCustomPlanUseCase {

	private final SavePlanPort savePlanPort;
	private final LoadMemberPort loadMemberPort;
	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public RegisterCustomPlanResponse register(
		Long memberId,
		RegisterCustomPlanRequest request,
		Long subscriptionId
	) {
		Member member = loadMemberPort.load(memberId);
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		Plan plan = new Plan(
			request.name(),
			request.amount(),
			AmountUnit.valueOf(request.amountUnit()),
			request.durationMonths(),
			false,
			member,
			subscription
		);

		return new RegisterCustomPlanResponse(savePlanPort.save(plan));
	}
}
