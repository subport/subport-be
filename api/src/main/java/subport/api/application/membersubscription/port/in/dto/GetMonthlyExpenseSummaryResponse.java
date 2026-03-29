package subport.api.application.membersubscription.port.in.dto;

import java.math.BigDecimal;

public record GetMonthlyExpenseSummaryResponse(
	BigDecimal currentMonthPaidAmount,
	BigDecimal currentMonthTotalAmount,
	int paymentProgressPercent
) {
}
