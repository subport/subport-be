package subport.application.membersubscription.port.out;

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
		BigDecimal amount = planAmount;
		if (dutchPay && dutchPayAmount != null) {
			amount = dutchPayAmount;
		}

		return SpendingRecord.withoutId(
			nextPaymentDate,
			amount,
			planDurationMonths,
			subscriptionName,
			subscriptionLogoImageUrl,
			memberId
		);
	}
}
