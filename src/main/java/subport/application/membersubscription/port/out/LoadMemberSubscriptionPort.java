package subport.application.membersubscription.port.out;

import java.time.LocalDate;
import java.util.List;

import subport.application.membersubscription.port.out.dto.MemberSubscriptionDetail;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionForMail;
import subport.application.membersubscription.port.out.dto.MemberSubscriptionForSpendingRecord;
import subport.domain.membersubscription.MemberSubscription;

public interface LoadMemberSubscriptionPort {

	MemberSubscription load(Long memberSubscriptionId);

	MemberSubscriptionDetail loadDetail(Long memberSubscriptionId);

	List<MemberSubscriptionDetail> loadDetails(
		Long memberId,
		boolean active,
		String sortBy
	);

	List<MemberSubscriptionForSpendingRecord> loadForSpendingRecordByNextPaymentDate(LocalDate currentDate);

	List<MemberSubscriptionForMail> loadForEmail(LocalDate currentDate);
}
