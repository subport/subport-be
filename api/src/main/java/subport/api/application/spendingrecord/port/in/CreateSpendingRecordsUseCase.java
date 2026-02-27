package subport.api.application.spendingrecord.port.in;

import java.time.LocalDateTime;

import subport.domain.membersubscription.MemberSubscription;

public interface CreateSpendingRecordsUseCase {

	void createMissing(MemberSubscription memberSubscription, LocalDateTime currentDateTime);
}
