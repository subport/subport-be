package subport.application.spendingrecord.port.out;

import java.util.List;

import subport.domain.spendingrecord.SpendingRecord;

public interface SaveSpendingRecordPort {

	void save(List<SpendingRecord> spendingRecords);
}
