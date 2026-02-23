package subport.admin.application.plan;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.application.membersubscription.MemberSubscriptionPort;
import subport.admin.application.plan.dto.RegisterPlanRequest;
import subport.admin.application.plan.dto.UpdatePlanRequest;
import subport.admin.application.subscription.SubscriptionPort;
import subport.domain.plan.AmountUnit;
import subport.domain.plan.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanCommandService {

	private final PlanPort planPort;
	private final MemberSubscriptionPort memberSubscriptionPort;
	private final SubscriptionPort subscriptionPort;

	@Transactional
	public void registerPlan(Long subscriptionId, RegisterPlanRequest request) {
		Subscription subscription = subscriptionPort.loadSubscription(subscriptionId);

		Plan plan = new Plan(
			request.name(),
			request.amount(),
			AmountUnit.valueOf(request.amountUnit()),
			request.durationMonths(),
			true,
			null,
			subscription
		);

		planPort.save(plan);
	}

	@Transactional
	public void updatePlan(Long planId, UpdatePlanRequest request) {
		Plan plan = planPort.loadPlan(planId);

		plan.update(
			request.name(),
			request.amount(),
			AmountUnit.valueOf(request.amountUnit()),
			request.durationMonths()
		);
	}

	@Transactional
	public void deletePlan(Long planId) {
		if (memberSubscriptionPort.existsByPlanId(planId)) {
			throw new subport.common.exception.CustomException(AdminErrorCode.PLAN_IN_USE);
		}

		Plan plan = planPort.loadPlan(planId);
		planPort.delete(plan);
	}
}
