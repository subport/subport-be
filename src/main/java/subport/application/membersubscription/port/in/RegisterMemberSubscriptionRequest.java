package subport.application.membersubscription.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterMemberSubscriptionRequest(
	LocalDate startDate,
	Integer reminderDaysBeforeEnd,
	String memo,
	boolean dutchPay,
	BigDecimal dutchPayAmount,
	Long subscriptionId,
	Long subscriptionPlanId
) {
}
