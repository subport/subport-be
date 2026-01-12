package subport.application.membersubscription.port.in;

import java.math.BigDecimal;

public record UpdateMemberSubscriptionDutchPayRequest(
	boolean dutchPay,
	BigDecimal dutchPayAmount
) {
}
