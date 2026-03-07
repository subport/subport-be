package subport.api.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.exchangeRate.service.ExchangeRateService;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.api.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.api.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.api.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.api.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.api.application.plan.port.out.LoadPlanPort;
import subport.api.application.spendingrecord.port.in.CreateSpendingRecordsUseCase;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.member.Member;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.plan.Plan;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterMemberSubscriptionService implements RegisterMemberSubscriptionUseCase {

	private final SaveMemberSubscriptionPort saveMemberSubscriptionPort;
	private final LoadSubscriptionPort loadSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final ExchangeRateService exchangeRateService;
	private final LoadMemberPort loadMemberPort;
	private final CreateSpendingRecordsUseCase createSpendingRecordsUseCase;

	@Override
	public RegisterMemberSubscriptionResponse register(
		Long memberId,
		RegisterMemberSubscriptionRequest request,
		LocalDateTime currentDateTime
	) {
		LocalDate startDate = request.startDate();
		validateStartDate(startDate, currentDateTime.toLocalDate());

		boolean dutchPay = request.dutchPay();
		BigDecimal dutchPayAmount = request.dutchPayAmount();
		if (dutchPay && dutchPayAmount == null) {
			throw new CustomException(ApiErrorCode.DUTCH_PAY_AMOUNT_MISSING);
		}
		if (!dutchPay && dutchPayAmount != null) {
			throw new CustomException(ApiErrorCode.DUTCH_PAY_AMOUNT_NOT_ALLOWED);
		}

		Long subscriptionId = request.subscriptionId();
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);
		if (!subscription.isSystemProvided() && !subscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.SUBSCRIPTION_USE_FORBIDDEN);
		}

		Plan plan = loadPlanPort.loadPlan(request.planId());
		if (!plan.getSubscription().getId().equals(subscriptionId)) {
			throw new CustomException(ApiErrorCode.PLAN_NOT_BELONG_TO_SUBSCRIPTION);
		}
		if (!plan.isSystemProvided() && !plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.PLAN_USE_FORBIDDEN);
		}

		BigDecimal rate = null;
		LocalDate exchangeRateDate = null;
		if (plan.isUsdBased()) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(startDate, currentDateTime);

			rate = exchangeRate.getRate();
			exchangeRateDate = exchangeRate.getApplyDate();
		}

		Member member = loadMemberPort.load(memberId);
		MemberSubscription memberSubscription = new MemberSubscription(
			startDate,
			request.memo(),
			dutchPay,
			dutchPayAmount,
			rate,
			exchangeRateDate,
			member,
			subscription,
			plan
		);

		MemberSubscription savedMemberSubscription = saveMemberSubscriptionPort.save(memberSubscription);
		createSpendingRecordsUseCase.createMissing(savedMemberSubscription, currentDateTime);

		return new RegisterMemberSubscriptionResponse(savedMemberSubscription.getId());
	}

	private void validateStartDate(LocalDate startDate, LocalDate currentDate) {
		if (startDate.isAfter(currentDate)) {
			throw new CustomException(ApiErrorCode.START_DATE_IN_FUTURE);
		}
		if (startDate.isBefore(currentDate.minusYears(1))) {
			throw new CustomException(ApiErrorCode.START_DATE_TOO_OLD);
		}
	}
}
