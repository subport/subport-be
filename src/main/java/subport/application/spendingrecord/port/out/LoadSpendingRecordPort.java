package subport.application.spendingrecord.port.out;

import java.time.LocalDate;
import java.util.List;

import subport.domain.spendingrecord.SpendingRecord;

public interface LoadSpendingRecordPort {

	SpendingRecord loadSpendingRecord(Long spendingRecordId);

	List<SpendingRecord> loadSpendingRecords(Long memberSubscriptionId);

	List<SpendingRecord> loadSpendingRecords(Long memberId, LocalDate start, LocalDate end);

	List<SpendingRecord> loadSpendingRecords(Long memberId, LocalDate targetDate);
}
