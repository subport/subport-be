package subport.application.membersubscription.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.membersubscription.port.in.ActivateMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.application.membersubscription.port.in.dto.ActivateMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivateMemberSubscriptionService implements ActivateMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final ExchangeRateService exchangeRateService;
	private final MemberSubscriptionQueryUseCase memberSubscriptionQueryUseCase;

	@Override
	public GetMemberSubscriptionResponse activate(
		Long memberId,
		ActivateMemberSubscriptionRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}
		if (memberSubscription.isActive()) {
			return memberSubscriptionQueryUseCase.getMemberSubscription(
				memberId,
				memberSubscriptionId,
				currentDateTime.toLocalDate()
			);
		}

		LocalDate startDate = request.startDate();
		memberSubscription.activate(startDate);

		Plan plan = memberSubscription.getPlan();
		Long newPlanId = request.planId();
		if (!request.reusePreviousInfo() && !plan.getId().equals(newPlanId)) {
			Plan newPlan = loadPlanPort.loadPlan(newPlanId);
			memberSubscription.updatePlan(newPlan);

			// 플랜 검증 로직 추가
		}

		if (plan.getAmountUnit().equals(AmountUnit.USD)) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(startDate, currentDateTime);
			memberSubscription.updateExchangeRate(
				exchangeRate.getRate(),
				exchangeRate.getApplyDate()
			);
		}

		if (request.reusePreviousInfo()) {
			return memberSubscriptionQueryUseCase.getMemberSubscription(
				memberId,
				memberSubscriptionId,
				currentDateTime.toLocalDate()
			);
		}

		memberSubscription.updateDutchPay(
			request.dutchPay(),
			request.dutchPayAmount()
		);

		memberSubscription.updateMemo(request.memo());

		return memberSubscriptionQueryUseCase.getMemberSubscription(
			memberId,
			memberSubscriptionId,
			currentDateTime.toLocalDate()
		);
	}
}
