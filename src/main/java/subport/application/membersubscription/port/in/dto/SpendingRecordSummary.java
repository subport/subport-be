package subport.application.membersubscription.port.in.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import subport.domain.spendingrecord.SpendingRecord;

public record SpendingRecordSummary(
	BigDecimal amount,
	LocalDate paymentDate
) {

	public static SpendingRecordSummary fromDomain(SpendingRecord spendingRecord) {
		return new SpendingRecordSummary(
			spendingRecord.getAmount(),
			spendingRecord.getPaymentDate()
		);
	}
}
