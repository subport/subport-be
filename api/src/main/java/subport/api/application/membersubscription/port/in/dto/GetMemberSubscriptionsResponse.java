package subport.api.application.membersubscription.port.in.dto;

import java.math.BigDecimal;

public record GetMemberSubscriptionsResponse(
	BigDecimal currentMonthPaidAmount,
	BigDecimal currentMonthTotalAmount,
	Integer paymentProgressPercent,
	Object subscriptions
) {
}
