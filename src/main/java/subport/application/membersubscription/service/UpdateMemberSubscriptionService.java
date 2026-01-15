package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionDutchPayRequest;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionDutchPayUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionMemoRequest;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionMemoUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanRequest;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionReminderRequest;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionReminderUseCase;
import subport.application.membersubscription.port.out.LoadExchangeRatePort;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.UpdateMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.Plan;
import subport.domain.subscription.SubscriptionAmountUnit;

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
	private final LoadExchangeRatePort loadExchangeRatePort;

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

		if (plan.getAmountUnit().name().equals(SubscriptionAmountUnit.USD.name())
			&& memberSubscription.getExchangeRate() == null
			&& memberSubscription.getExchangeRateDate() == null) {
			LocalDate exchangeRateDate = memberSubscription.getNextPaymentDate()
				.minusMonths(plan.getDurationMonths());

			BigDecimal exchangeRate = loadExchangeRatePort.load(
				exchangeRateDate.format(DateTimeFormatter.BASIC_ISO_DATE)
			);

			memberSubscription.updateExchangeRate(exchangeRate, exchangeRateDate);
		}

		memberSubscription.updatePlan(newPlanId);

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
