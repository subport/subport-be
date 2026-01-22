package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionDutchPayUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionMemoUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionReminderUseCase;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionDutchPayRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionMemoRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionPlanRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionReminderRequest;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberSubscriptionService implements
	UpdateMemberSubscriptionPlanUseCase,
	UpdateMemberSubscriptionDutchPayUseCase,
	UpdateMemberSubscriptionReminderUseCase,
	UpdateMemberSubscriptionMemoUseCase {

	private final LoadMemberSubscriptionPort loadMemberSubscriptionPort;
	private final UpdateMemberSubscriptionPort updateMemberSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final ExchangeRateService exchangeRateService;

	@Override
	public void updatePlan(
		Long memberId,
		UpdateMemberSubscriptionPlanRequest request,
		Long memberSubscriptionId
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.load(memberSubscriptionId);

		Long newPlanId = request.planId();
		if (newPlanId.equals(memberSubscription.getPlanId())) {
			return;
		}

		if (!memberSubscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		Plan newPlan = loadPlanPort.load(newPlanId);
		if (!newPlan.getSubscriptionId().equals(memberSubscription.getSubscriptionId())) {
			throw new CustomException(ErrorCode.INVALID_MEMBER_SUBSCRIPTION_PLAN);
		}

		if (!newPlan.isSystemProvided() && !newPlan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_USE_FORBIDDEN);
		}

		String amountUnitName = newPlan.getAmountUnit().name();
		LocalDate lastPaymentDate = memberSubscription.getLastPaymentDate();
		BigDecimal rate = null;
		LocalDate exchangeRateDate = null;
		if (amountUnitName.equals(AmountUnit.USD.name())
			&& memberSubscription.getExchangeRate() == null
			&& memberSubscription.getExchangeRateDate() == null) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(lastPaymentDate);

			rate = exchangeRate.getRate();
			exchangeRateDate = exchangeRate.getApplyDate();
		}
		memberSubscription.updateExchangeRate(rate, exchangeRateDate);

		memberSubscription.updatePlan(newPlanId);
		memberSubscription.updateNextPaymentDate(
			lastPaymentDate.plusMonths(newPlan.getDurationMonths())
		);

		updateMemberSubscriptionPort.update(memberSubscription);
	}

	@Override
	public void updateDutchPay(
		Long memberId,
		UpdateMemberSubscriptionDutchPayRequest request,
		Long memberSubscriptionId
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.load(memberSubscriptionId);

		if (!memberSubscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		boolean dutchPay = request.dutchPay();
		BigDecimal dutchPayAmount = request.dutchPayAmount();
		if (dutchPay && dutchPayAmount == null) {
			throw new CustomException(ErrorCode.DUTCH_PAY_AMOUNT_MISSING);
		}
		if (!dutchPay && dutchPayAmount != null) {
			throw new CustomException(ErrorCode.DUTCH_PAY_AMOUNT_NOT_ALLOWED);
		}

		memberSubscription.updateDutchPay(dutchPay, dutchPayAmount);

		updateMemberSubscriptionPort.update(memberSubscription);
	}

	@Override
	public void updateReminder(
		Long memberId,
		UpdateMemberSubscriptionReminderRequest request,
		Long memberSubscriptionId
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.load(memberSubscriptionId);

		if (!memberSubscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		memberSubscription.updateReminderDaysBefore(request.reminderDaysBefore());

		updateMemberSubscriptionPort.update(memberSubscription);
	}

	@Override
	public void updateMemo(
		Long memberId,
		UpdateMemberSubscriptionMemoRequest request,
		Long memberSubscriptionId
	) {
		MemberSubscription memberSubscription = loadMemberSubscriptionPort.load(memberSubscriptionId);

		if (!memberSubscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_FORBIDDEN);
		}

		memberSubscription.updateMemo(request.memo());

		updateMemberSubscriptionPort.update(memberSubscription);
	}
}
