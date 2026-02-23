package subport.api.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.exchangeRate.service.ExchangeRateService;
import subport.api.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.api.application.membersubscription.port.in.UpdateMemberSubscriptionDutchPayUseCase;
import subport.api.application.membersubscription.port.in.UpdateMemberSubscriptionMemoUseCase;
import subport.api.application.membersubscription.port.in.UpdateMemberSubscriptionPlanUseCase;
import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.api.application.membersubscription.port.in.dto.UpdateMemberSubscriptionDutchPayRequest;
import subport.api.application.membersubscription.port.in.dto.UpdateMemberSubscriptionMemoRequest;
import subport.api.application.membersubscription.port.in.dto.UpdateMemberSubscriptionPlanRequest;
import subport.api.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.common.exception.CustomException;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.plan.AmountUnit;
import subport.domain.plan.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberSubscriptionService implements
	UpdateMemberSubscriptionPlanUseCase,
	UpdateMemberSubscriptionDutchPayUseCase,
	UpdateMemberSubscriptionMemoUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final ExchangeRateService exchangeRateService;
	private final MemberSubscriptionQueryUseCase memberSubscriptionQueryUseCase;

	@Override
	public GetMemberSubscriptionResponse updatePlan(
		Long memberId,
		UpdateMemberSubscriptionPlanRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		Long newPlanId = request.planId();
		if (newPlanId.equals(memberSubscription.getPlan().getId())) {
			return memberSubscriptionQueryUseCase.getMemberSubscription(
				memberId,
				memberSubscriptionId,
				currentDateTime.toLocalDate()
			);
		}

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		Plan newPlan = loadPlanPort.loadPlan(newPlanId);
		if (!newPlan.getSubscription().getId().equals(memberSubscription.getSubscription().getId())) {
			throw new CustomException(ApiErrorCode.INVALID_MEMBER_SUBSCRIPTION_PLAN);
		}

		if (!newPlan.isSystemProvided() && !newPlan.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.PLAN_USE_FORBIDDEN);
		}

		String amountUnitName = newPlan.getAmountUnit().name();
		LocalDate lastPaymentDate = memberSubscription.getLastPaymentDate();
		BigDecimal rate = null;
		LocalDate exchangeRateDate = null;
		if (amountUnitName.equals(AmountUnit.USD.name())
			&& memberSubscription.getExchangeRate() == null
			&& memberSubscription.getExchangeRateDate() == null) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(lastPaymentDate, currentDateTime);

			rate = exchangeRate.getRate();
			exchangeRateDate = exchangeRate.getApplyDate();
		}
		memberSubscription.updateExchangeRate(rate, exchangeRateDate);

		memberSubscription.updatePlan(newPlan);

		return memberSubscriptionQueryUseCase.getMemberSubscription(
			memberId,
			memberSubscriptionId,
			currentDateTime.toLocalDate()
		);
	}

	@Override
	public GetMemberSubscriptionResponse updateDutchPay(
		Long memberId,
		UpdateMemberSubscriptionDutchPayRequest request,
		Long memberSubscriptionId,
		LocalDate currentDate
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		boolean dutchPay = request.dutchPay();
		BigDecimal dutchPayAmount = request.dutchPayAmount();
		if (dutchPay && dutchPayAmount == null) {
			throw new CustomException(ApiErrorCode.DUTCH_PAY_AMOUNT_MISSING);
		}
		if (!dutchPay && dutchPayAmount != null) {
			throw new CustomException(ApiErrorCode.DUTCH_PAY_AMOUNT_NOT_ALLOWED);
		}

		memberSubscription.updateDutchPay(dutchPay, dutchPayAmount);

		return memberSubscriptionQueryUseCase.getMemberSubscription(
			memberId,
			memberSubscriptionId,
			currentDate
		);
	}

	@Override
	public GetMemberSubscriptionResponse updateMemo(
		Long memberId,
		UpdateMemberSubscriptionMemoRequest request,
		Long memberSubscriptionId,
		LocalDate currentDate
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.loadMemberSubscription(memberSubscriptionId);

		if (!memberSubscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		memberSubscription.updateMemo(request.memo());

		return memberSubscriptionQueryUseCase.getMemberSubscription(
			memberId,
			memberSubscriptionId,
			currentDate
		);
	}
}
