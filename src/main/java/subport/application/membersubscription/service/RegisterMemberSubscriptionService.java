package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;
import subport.domain.subscription.Subscription;

@Service
@RequiredArgsConstructor
public class RegisterMemberSubscriptionService implements RegisterMemberSubscriptionUseCase {

	private final SaveMemberSubscriptionPort saveMemberSubscriptionPort;
	private final LoadSubscriptionPort loadSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final ExchangeRateService exchangeRateService;

	@Transactional
	@Override
	public RegisterMemberSubscriptionResponse register(Long memberId, RegisterMemberSubscriptionRequest request) {
		boolean dutchPay = request.dutchPay();
		BigDecimal dutchPayAmount = request.dutchPayAmount();
		if (dutchPay && dutchPayAmount == null) {
			throw new CustomException(ErrorCode.DUTCH_PAY_AMOUNT_MISSING);
		}
		if (!dutchPay && dutchPayAmount != null) {
			throw new CustomException(ErrorCode.DUTCH_PAY_AMOUNT_NOT_ALLOWED);
		}

		Long subscriptionId = request.subscriptionId();
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);
		if (!subscription.isSystemProvided() && !subscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_USE_FORBIDDEN);
		}

		BigDecimal rate = null;
		LocalDate exchangeRateDate = null;
		Plan plan = loadPlanPort.load(request.planId());

		if (!plan.getSubscriptionId().equals(subscriptionId)) {
			throw new CustomException(ErrorCode.INVALID_MEMBER_SUBSCRIPTION_PLAN);
		}

		if (!plan.isSystemProvided() && !plan.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_USE_FORBIDDEN);
		}

		LocalDate startDate = request.startDate();
		if (plan.getAmountUnit().name().equals(AmountUnit.USD.name())) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(startDate);

			rate = exchangeRate.getRate();
			exchangeRateDate = exchangeRate.getApplyDate();
		}

		LocalDate nextPaymentDate = startDate.plusMonths(plan.getDurationMonths());
		MemberSubscription memberSubscription = MemberSubscription.withoutId(
			startDate,
			request.reminderDaysBefore(),
			request.memo(),
			dutchPay,
			dutchPayAmount,
			rate,
			exchangeRateDate,
			true,
			startDate,
			nextPaymentDate,
			memberId,
			subscriptionId,
			request.planId()
		);

		return new RegisterMemberSubscriptionResponse(saveMemberSubscriptionPort.save(memberSubscription));
	}
}
