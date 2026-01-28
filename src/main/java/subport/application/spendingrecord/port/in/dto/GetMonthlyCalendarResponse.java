package subport.application.spendingrecord.port.in.dto;

import java.math.BigDecimal;
import java.util.Map;

public record GetMonthlyCalendarResponse(
	BigDecimal totalAmount,
	BigDecimal amountDiffFromPreviousMonth,
	Map<Integer, PaymentDateInfo> paymentDates
) {
}
