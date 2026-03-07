package subport.api.application.membersubscription.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.exchangeRate.service.ExchangeRateService;
import subport.api.application.membersubscription.port.in.ActivateMemberSubscriptionUseCase;
import subport.api.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.api.application.membersubscription.port.in.dto.ActivateMemberSubscriptionRequest;
import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.api.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.api.application.spendingrecord.port.in.CreateSpendingRecordsUseCase;
import subport.common.exception.CustomException;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.plan.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivateMemberSubscriptionService implements ActivateMemberSubscriptionUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final ExchangeRateService exchangeRateService;
	private final MemberSubscriptionQueryUseCase memberSubscriptionQueryUseCase;
	private final CreateSpendingRecordsUseCase createSpendingRecordsUseCase;

	@Override
	public GetMemberSubscriptionResponse activate(
		Long memberId,
		ActivateMemberSubscriptionRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		LocalDate startDate = request.startDate();
		if (startDate.isBefore(memberSubscription.getLastPaymentDate())) {
			throw new CustomException(ApiErrorCode.REACTIVATION_START_DATE_BEFORE_LAST_PAYMENT);
		}
		if (startDate.isAfter(currentDateTime.toLocalDate())) {
			throw new CustomException(ApiErrorCode.START_DATE_IN_FUTURE);
		}
		if (startDate.isBefore(currentDateTime.toLocalDate().minusYears(1))) {
			throw new CustomException(ApiErrorCode.START_DATE_TOO_OLD);
		}

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_ACCESS_FORBIDDEN);
		}
		if (memberSubscription.isActive()) {
			return memberSubscriptionQueryUseCase.getMemberSubscription(
				memberId,
				memberSubscriptionId,
				currentDateTime.toLocalDate()
			);
		}

		memberSubscription.activate(startDate);

		Plan plan = memberSubscription.getPlan();
		Long newPlanId = request.planId();
		if (!request.reusePreviousInfo() && !plan.getId().equals(newPlanId)) {
			Plan newPlan = loadPlanPort.loadPlan(newPlanId);
			memberSubscription.updatePlan(newPlan);

			// 플랜 검증 로직 추가
		}

		if (plan.isUsdBased()) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(startDate, currentDateTime);
			memberSubscription.updateExchangeRate(
				exchangeRate.getRate(),
				exchangeRate.getApplyDate()
			);
		}

		if (request.reusePreviousInfo()) {
			createSpendingRecordsUseCase.createMissing(memberSubscription, currentDateTime);

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

		createSpendingRecordsUseCase.createMissing(memberSubscription, currentDateTime);

		return memberSubscriptionQueryUseCase.getMemberSubscription(
			memberId,
			memberSubscriptionId,
			currentDateTime.toLocalDate()
		);
	}
}
