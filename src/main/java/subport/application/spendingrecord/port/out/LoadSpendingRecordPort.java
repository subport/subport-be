package subport.application.spendingrecord.port.out;

import java.util.List;

import subport.domain.spendingrecord.SpendingRecord;

public interface LoadSpendingRecordPort {

	List<SpendingRecord> loadRecent3ByMemberIdAndSubscriptionName(Long memberId, String subscriptionName);
}
