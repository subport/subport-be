package subport.application.spendingrecord.port.in;

import java.time.LocalDateTime;

public interface CreateSpendingRecordsUseCase {

	void create(LocalDateTime currentDateTime);
}
