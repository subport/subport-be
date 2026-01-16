package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.application.membersubscription.port.out.LoadExchangeRatePort;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.Plan;
import subport.domain.subscription.SubscriptionAmountUnit;

@Service
@RequiredArgsConstructor
public class RegisterMemberSubscriptionService implements RegisterMemberSubscriptionUseCase {

	private final SaveMemberSubscriptionPort saveMemberSubscriptionPort;
	private final LoadPlanPort loadPlanPort;
	private final LoadExchangeRatePort loadExchangeRatePort;

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

		LocalDate startDate = request.startDate();
		BigDecimal exchangerRate = null;
		Plan plan = loadPlanPort.load(request.planId());
		if (plan.getAmountUnit().name().equals(SubscriptionAmountUnit.USD.name())) {
			exchangerRate = loadExchangeRatePort.load(startDate.toString());
		}

		LocalDate nextPaymentDate = startDate.plusMonths(plan.getDurationMonths());
		MemberSubscription memberSubscription = MemberSubscription.withoutId(
			startDate,
			request.reminderDaysBefore(),
			request.memo(),
			dutchPay,
			dutchPayAmount,
			exchangerRate,
			startDate,
			true,
			nextPaymentDate,
			memberId,
			request.subscriptionId(),
			request.planId()
		);

		return new RegisterMemberSubscriptionResponse(saveMemberSubscriptionPort.save(memberSubscription));
	}
}
