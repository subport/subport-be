package subport.application.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminPlanResponse;
import subport.application.admin.dto.AdminPlansResponse;
import subport.application.admin.dto.AdminRegisterPlanRequest;
import subport.application.admin.dto.AdminUpdatePlanRequest;
import subport.application.admin.port.AdminMemberSubscriptionPort;
import subport.application.admin.port.AdminPlanPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeletePlanPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SavePlanPort;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminPlanService {

	private final AdminPlanPort planPort;
	private final LoadPlanPort loadPlanPort;
	private final DeletePlanPort deletePlanPort;
	private final AdminMemberSubscriptionPort memberSubscriptionPort;
	private final LoadSubscriptionPort loadSubscriptionPort;
	private final SavePlanPort savePlanPort;

	@Transactional
	public void registerPlan(Long subscriptionId, AdminRegisterPlanRequest request) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		Plan plan = new Plan(
			request.name(),
			request.amount(),
			AmountUnit.fromString(request.amountUnit()),
			request.durationMonths(),
			true,
			null,
			subscription
		);

		savePlanPort.save(plan);
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
		Plan plan = loadPlanPort.loadPlan(planId);

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

		Plan plan = loadPlanPort.loadPlan(planId);
		deletePlanPort.delete(plan);
	}
}
