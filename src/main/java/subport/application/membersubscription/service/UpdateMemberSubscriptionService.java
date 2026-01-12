package subport.application.membersubscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanRequest;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanUseCase;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberSubscriptionService implements UpdateMemberSubscriptionPlanUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final UpdateMemberSubscriptionPort updateMemberSubscriptionPort;

	private final LoadPlanPort loadPlanPort;

	@Override
	public void updatePlan(
		Long memberId,
		UpdateMemberSubscriptionPlanRequest request,
		Long memberSubscriptionId
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.load(memberSubscriptionId);

		if (!memberSubscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		Long newPlanId = request.planId();
		Plan plan = loadPlanPort.load(newPlanId);
		if (!plan.getSubscriptionId().equals(memberSubscription.getSubscriptionId())) {
			throw new CustomException(ErrorCode.INVALID_MEMBER_SUBSCRIPTION_PLAN);
		}

		if (!plan.isSystemProvided() && !plan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_USE_FORBIDDEN);
		}

		memberSubscription.updatePlan(newPlanId);

		updateMemberSubscriptionPort.update(memberSubscription);
	}
}
