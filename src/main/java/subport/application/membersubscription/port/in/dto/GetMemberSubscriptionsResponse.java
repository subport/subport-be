package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;

public record GetMemberSubscriptionsResponse(
	BigDecimal totalAmount,
	Object subscriptions
) {
}
