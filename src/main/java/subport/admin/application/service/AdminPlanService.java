package subport.admin.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminPlanResponse;
import subport.admin.application.dto.AdminPlansResponse;
import subport.admin.application.dto.AdminRegisterPlanRequest;
import subport.admin.application.dto.AdminUpdatePlanRequest;
import subport.admin.application.port.AdminMemberSubscriptionPort;
import subport.admin.application.port.AdminPlanPort;
import subport.admin.application.port.AdminSubscriptionPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminPlanService {

	private final AdminPlanPort planPort;
	private final AdminMemberSubscriptionPort memberSubscriptionPort;
	private final AdminSubscriptionPort subscriptionPort;

	@Transactional
	public void registerPlan(Long subscriptionId, AdminRegisterPlanRequest request) {
		Subscription subscription = subscriptionPort.loadSubscription(subscriptionId);

		Plan plan = new Plan(
			request.name(),
			request.amount(),
			AmountUnit.fromString(request.amountUnit()),
			request.durationMonths(),
			true,
			null,
			subscription
		);

		planPort.save(plan);
	}

	public AdminPlansResponse getPlans(Long subscriptionId) {
		return new AdminPlansResponse(
			planPort.loadPlans(subscriptionId).stream()
				.map(AdminPlanResponse::from)
				.toList()
		);
	}

	@Transactional
	public void updatePlan(Long planId, AdminUpdatePlanRequest request) {
		Plan plan = planPort.loadPlan(planId);

		plan.update(
			request.name(),
			request.amount(),
			AmountUnit.fromString(request.amountUnit()),
			request.durationMonths()
		);
	}

	@Transactional
	public void deletePlan(Long planId) {
		if (memberSubscriptionPort.existsByPlanId(planId)) {
			throw new CustomException(ErrorCode.PLAN_IN_USE);
		}

		Plan plan = planPort.loadPlan(planId);
		planPort.delete(plan);
	}
}
