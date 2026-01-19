package subport.application.membersubscription.port.out;

import java.time.LocalDate;
import java.util.List;

import subport.application.membersubscription.port.out.dto.MemberSubscriptionForSpendingRecord;
import subport.domain.membersubscription.MemberSubscription;

public interface LoadMemberSubscriptionPort {

	MemberSubscription load(Long memberSubscriptionId);

	List<MemberSubscriptionForSpendingRecord> loadForSpendingRecordByNextPaymentDate(LocalDate currentDate);
}
