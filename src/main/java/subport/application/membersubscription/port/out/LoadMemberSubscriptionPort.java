package subport.application.membersubscription.port.out;

import java.time.LocalDate;
import java.util.List;

import subport.domain.membersubscription.MemberSubscription;

public interface LoadMemberSubscriptionPort {

	MemberSubscription load(Long memberSubscriptionId);

	List<MemberSubscriptionDetail> loadDetailsByNextPaymentDate(LocalDate currentDate);
}
