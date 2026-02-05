package subport.application.spendingrecord.port.in;

import java.time.LocalDateTime;

import subport.domain.membersubscription.MemberSubscription;

public interface CreateSpendingRecordsUseCase {

	void createForScheduling(LocalDateTime currentDateTime);

	void createMissing(MemberSubscription memberSubscription, LocalDateTime currentDateTime);
}
