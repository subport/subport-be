package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.application.membersubscription.port.out.LoadExchangeRatePort;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.Plan;

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
		LocalDate startBusinessDate = getLastBusinessDate(startDate);

		BigDecimal exchangerRate = null;
		LocalDate exchangeRateDate = null;
		Plan plan = loadPlanPort.load(request.planId());
		if (plan.getAmountUnit().name().equals(AmountUnit.USD.name())) {
			exchangeRateDate = startBusinessDate;

			if (!startBusinessDate.isAfter(getLastBusinessDate(LocalDate.now()))) {
				LocalDate targetDate = startBusinessDate;

				while (exchangerRate == null) {
					exchangerRate = loadExchangeRatePort.load(targetDate.toString());
					if (exchangerRate == null) {
						targetDate = targetDate.minusDays(1);
					}
				}
				
				exchangeRateDate = targetDate;
			}
		}

		LocalDate nextPaymentDate = startDate.plusMonths(plan.getDurationMonths());
		MemberSubscription memberSubscription = MemberSubscription.withoutId(
			startDate,
			request.reminderDaysBefore(),
			request.memo(),
			dutchPay,
			dutchPayAmount,
			exchangerRate,
			exchangeRateDate,
			true,
			nextPaymentDate,
			memberId,
			request.subscriptionId(),
			request.planId()
		);

		return new RegisterMemberSubscriptionResponse(saveMemberSubscriptionPort.save(memberSubscription));
	}

	private LocalDate getLastBusinessDate(LocalDate date) {
		return switch (date.getDayOfWeek()) {
			case SATURDAY -> date.minusDays(1);
			case SUNDAY -> date.minusDays(2);
			default -> date;
		};
	}
}
