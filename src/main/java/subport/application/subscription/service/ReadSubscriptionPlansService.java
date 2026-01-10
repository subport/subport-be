package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.ReadSubscriptionPlansUseCase;
import subport.application.subscription.port.out.ListSubscriptionPlansResponse;
import subport.application.subscription.port.out.LoadSubscriptionPlanPort;
import subport.application.subscription.port.out.ReadSubscriptionPlanResponse;
import subport.domain.subscription.SubscriptionPlan;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadSubscriptionPlansService implements ReadSubscriptionPlansUseCase {

	private final LoadSubscriptionPlanPort loadSubscriptionPlanPort;

	@Override
	public ReadSubscriptionPlanResponse read(Long memberId, Long planId) {
		SubscriptionPlan plan = loadSubscriptionPlanPort.load(planId);

		if (!plan.isSystemProvided() && !plan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_READ_FORBIDDEN);
		}

		return ReadSubscriptionPlanResponse.fromDomain(plan);
	}

	@Override
	public ListSubscriptionPlansResponse list(Long memberId, Long subscriptionId) {
		return ListSubscriptionPlansResponse.fromDomains(
			loadSubscriptionPlanPort.loadByMemberIdAndSubscriptionId(memberId, subscriptionId));
	}
}
