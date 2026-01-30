package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.subscription.port.in.RegisterCustomPlanUseCase;
import subport.application.subscription.port.in.dto.RegisterCustomPlanRequest;
import subport.application.subscription.port.in.dto.RegisterCustomPlanResponse;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SavePlanPort;
import subport.domain.member.Member;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;
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
			AmountUnit.fromString(request.amountUnit()),
			request.durationMonths(),
			false,
			member,
			subscription
		);

		return new RegisterCustomPlanResponse(savePlanPort.save(plan));
	}
}
