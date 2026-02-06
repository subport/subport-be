package subport.application.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminPlanResponse;
import subport.application.admin.dto.AdminPlansResponse;
import subport.application.admin.dto.AdminUpdatePlanRequest;
import subport.application.admin.port.AdminPlanPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminPlanService {

	private final AdminPlanPort planPort;
	private final LoadPlanPort loadPlanPort;

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
}
