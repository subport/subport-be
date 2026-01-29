package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;

public record GetMemberSubscriptionsResponse(
	BigDecimal currentMonthTotalAmount,
	Object subscriptions
) {
}
