package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;

public record ListMemberSubscriptionsResponse(
	BigDecimal totalAmount,
	Object subscriptions
) {
}
