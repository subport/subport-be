package subport.application.membersubscription.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.exchangeRate.service.ExchangeRateService;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.application.spendingrecord.port.out.LoadSpendingRecordPort;
import subport.application.spendingrecord.port.out.SaveSpendingRecordPort;
import subport.application.subscription.port.out.LoadPlanPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.domain.exchangeRate.ExchangeRate;
import subport.domain.member.Member;
import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;
import subport.domain.subscription.Plan;
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
	private final LoadSpendingRecordPort loadSpendingRecordPort;
	private final SaveSpendingRecordPort saveSpendingRecordPort;

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
			throw new CustomException(ErrorCode.DUTCH_PAY_AMOUNT_MISSING);
		}
		if (!dutchPay && dutchPayAmount != null) {
			throw new CustomException(ErrorCode.DUTCH_PAY_AMOUNT_NOT_ALLOWED);
		}

		Long subscriptionId = request.subscriptionId();
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);
		if (!subscription.isSystemProvided() && !subscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_USE_FORBIDDEN);
		}

		Plan plan = loadPlanPort.loadPlan(request.planId());
		if (!plan.getSubscription().getId().equals(subscriptionId)) {
			throw new CustomException(ErrorCode.INVALID_MEMBER_SUBSCRIPTION_PLAN);
		}
		if (!plan.isSystemProvided() && !plan.getMember().getId().equals(memberId)) {
			throw new CustomException(ErrorCode.PLAN_USE_FORBIDDEN);
		}

		BigDecimal rate = null;
		LocalDate exchangeRateDate = null;
		if (plan.isUsdBased()) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(startDate, currentDateTime);

			rate = exchangeRate.getRate();
			exchangeRateDate = exchangeRate.getApplyDate();
		}

		Member member = loadMemberPort.load(memberId);
		Integer reminderDaysBefore = request.reminderDaysBefore();
		LocalDate nextPaymentDate = startDate.plusMonths(plan.getDurationMonths());
		MemberSubscription memberSubscription = new MemberSubscription(
			startDate,
			reminderDaysBefore,
			request.memo(),
			dutchPay,
			dutchPayAmount,
			rate,
			exchangeRateDate,
			startDate,
			nextPaymentDate,
			member,
			subscription,
			plan
		);

		MemberSubscription savedMemberSubscription = saveMemberSubscriptionPort.save(memberSubscription);

		List<SpendingRecord> spendingRecords = createSpendingRecords(savedMemberSubscription, currentDateTime);
		saveSpendingRecordPort.save(spendingRecords);

		return new RegisterMemberSubscriptionResponse(savedMemberSubscription.getId());
	}

	private void validateStartDate(LocalDate startDate, LocalDate currentDate) {
		if (startDate.isAfter(currentDate)) {
			throw new CustomException(ErrorCode.INVALID_START_DATE_FUTURE);
		}
		if (startDate.isBefore(currentDate.minusYears(1))) {
			throw new CustomException(ErrorCode.INVALID_START_DATE_TOO_OLD);
		}
	}

	private List<SpendingRecord> createSpendingRecords(
		MemberSubscription memberSubscription,
		LocalDateTime currentDateTime
	) {
		List<SpendingRecord> spendingRecords = new ArrayList<>();
		LocalDate nextPaymentDate = memberSubscription.getNextPaymentDate();
		while (!nextPaymentDate.isAfter(currentDateTime.toLocalDate())) {
			SpendingRecord spendingRecord;
			if (nextPaymentDate.isEqual(currentDateTime.toLocalDate())
				&& !loadSpendingRecordPort.existsSpendingRecord(nextPaymentDate, memberSubscription.getId())) {
				spendingRecord = createSpendingRecord(memberSubscription, currentDateTime);
				spendingRecords.add(spendingRecord);
				break;
			}

			spendingRecord = createSpendingRecord(memberSubscription, currentDateTime);
			spendingRecords.add(spendingRecord);

			nextPaymentDate = memberSubscription.getNextPaymentDate();
		}

		return spendingRecords;
	}

	private SpendingRecord createSpendingRecord(MemberSubscription memberSubscription, LocalDateTime currentDateTime) {
		Subscription subscription = memberSubscription.getSubscription();
		SpendingRecord spendingRecord = new SpendingRecord(
			memberSubscription.getLastPaymentDate(),
			memberSubscription.calculateActualPaymentAmount(),
			memberSubscription.getPlan().getDurationMonths(),
			subscription.getName(),
			subscription.getLogoImageUrl(),
			memberSubscription.getMember().getId(),
			memberSubscription.getId()
		);

		memberSubscription.updateLastPaymentDate();

		if (memberSubscription.getExchangeRate() != null) {
			ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(
				memberSubscription.getLastPaymentDate(),
				currentDateTime
			);

			memberSubscription.updateExchangeRate(
				exchangeRate.getRate(),
				exchangeRate.getApplyDate()
			);
		}

		return spendingRecord;
	}
}
