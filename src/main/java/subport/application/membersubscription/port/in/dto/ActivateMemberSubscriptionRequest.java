package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record ActivateMemberSubscriptionRequest(
	boolean reusePreviousInfo,
	Long planId,
	Boolean dutchPay,
	BigDecimal dutchPayAmount,
	Integer reminderDaysBefore,
	String memo,

	@NotNull
	LocalDate startDate
) {
}
