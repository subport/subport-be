package subport.application.spendingrecord.port.in;

import java.time.LocalDateTime;

public interface CreateSpendingRecordUseCase {

	void create(LocalDateTime currentDateTime);
}
