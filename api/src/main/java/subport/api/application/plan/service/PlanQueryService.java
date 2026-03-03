package subport.api.application.plan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.plan.port.in.PlanQueryUseCase;
import subport.api.application.plan.port.in.dto.GetPlanResponse;
import subport.api.application.plan.port.in.dto.GetPlansResponse;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.plan.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanQueryService implements PlanQueryUseCase {

	private final LoadPlanPort loadPlanPort;
	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public GetPlanResponse getPlan(Long memberId, Long planId) {
		Plan plan = loadPlanPort.loadPlan(planId);

		if (!plan.isSystemProvided() && !plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.PLAN_READ_FORBIDDEN);
		}

		return GetPlanResponse.from(plan);
	}

	@Override
	public GetPlansResponse getPlans(Long memberId, Long subscriptionId) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		return GetPlansResponse.of(
			subscription.getPlanUrl(),
			loadPlanPort.loadPlans(memberId, subscriptionId)
		);
	}
}
