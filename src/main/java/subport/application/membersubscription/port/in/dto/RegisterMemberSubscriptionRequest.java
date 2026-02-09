package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterMemberSubscriptionRequest(
	@NotNull
	LocalDate startDate,

	@Size(max = 100)
	String memo,

	boolean dutchPay,

	BigDecimal dutchPayAmount,

	@NotNull
	Long subscriptionId,

	@NotNull
	Long planId
) {
}
