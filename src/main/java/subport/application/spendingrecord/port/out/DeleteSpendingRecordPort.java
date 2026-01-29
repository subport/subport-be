package subport.application.spendingrecord.port.out;

import subport.domain.spendingrecord.SpendingRecord;

public interface DeleteSpendingRecordPort {

	void delete(SpendingRecord spendingRecord);
}
