package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.ReadPlanUseCase;
import subport.application.subscription.port.out.ListPlansResponse;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.ReadPlanResponse;
import subport.domain.subscription.Plan;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadPlanService implements ReadPlanUseCase {

	private final LoadPlanPort loadPlanPort;
	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public ReadPlanResponse read(Long memberId, Long planId) {
		Plan plan = loadPlanPort.load(planId);

		if (!plan.isSystemProvided() && !plan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_READ_FORBIDDEN);
		}

		return ReadPlanResponse.fromDomain(plan);
	}

	@Override
	public ListPlansResponse list(Long memberId, Long subscriptionId) {
		loadSubscriptionPort.load(subscriptionId);

		return ListPlansResponse.fromDomains(
			loadPlanPort.loadByMemberIdAndSubscriptionId(memberId, subscriptionId));
	}
}
