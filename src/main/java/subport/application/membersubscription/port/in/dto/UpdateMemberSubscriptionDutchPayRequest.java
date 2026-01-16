package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;

public record UpdateMemberSubscriptionDutchPayRequest(
	boolean dutchPay,
	BigDecimal dutchPayAmount
) {
}
