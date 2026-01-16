package subport.application.membersubscription.port.out.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import subport.domain.membersubscription.MemberSubscription;
import subport.domain.spendingrecord.SpendingRecord;

public record MemberSubscriptionDetail(
	Long id,
	LocalDate startDate,
	Integer reminderDaysBefore,
	String memo,
	boolean dutchPay,
	BigDecimal dutchPayAmount,
	BigDecimal exchangeRate,
	LocalDate exchangeRateDate,
	boolean active,
	LocalDate nextPaymentDate,
	Long memberId,
	Long subscriptionId,
	Long planId,
	BigDecimal planAmount,
	int planDurationMonths,
	String subscriptionName,
	String subscriptionLogoImageUrl
) {

	public MemberSubscription toMemberSubscription() {
		return MemberSubscription.withId(
			id,
			startDate,
			reminderDaysBefore,
			memo,
			dutchPay,
			dutchPayAmount,
			exchangeRate,
			exchangeRateDate,
			active,
			nextPaymentDate,
			memberId,
			subscriptionId,
			planId
		);
	}

	public SpendingRecord toSpendingRecord() {
		return SpendingRecord.withoutId(
			nextPaymentDate,
			resolveAmount(),
			planDurationMonths,
			subscriptionName,
			subscriptionLogoImageUrl,
			memberId
		);
	}

	private BigDecimal resolveAmount() {
		if (dutchPay && dutchPayAmount != null) {
			return dutchPayAmount;
		}

		if (exchangeRate != null) {
			return planAmount.multiply(exchangeRate);
		}

		return planAmount;
	}
}
