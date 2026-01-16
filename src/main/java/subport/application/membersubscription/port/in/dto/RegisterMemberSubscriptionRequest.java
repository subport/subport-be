package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterMemberSubscriptionRequest(
	LocalDate startDate,
	Integer reminderDaysBefore,
	String memo,
	boolean dutchPay,
	BigDecimal dutchPayAmount,
	Long subscriptionId,
	Long planId
) {
}
