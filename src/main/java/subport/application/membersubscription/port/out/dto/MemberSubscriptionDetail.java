package subport.application.membersubscription.port.out.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import subport.domain.subscription.AmountUnit;
import subport.domain.subscription.SubscriptionType;

public record MemberSubscriptionDetail(
	Long id,
	Long subscriptionId,
	String subscriptionName,
	String subscriptionLogoImageUrl,
	SubscriptionType subscriptionType,
	LocalDate startDate,
	LocalDate nextPaymentDate,
	Integer reminderDaysBefore,
	boolean active,
	boolean dutchPay,
	BigDecimal dutchPayAmount,
	String memo,
	BigDecimal exchangeRate,
	Long planId,
	String planName,
	BigDecimal planAmount,
	AmountUnit planAmountUnit,
	int durationMonths,
	Long memberId
) {
}
