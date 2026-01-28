package subport.application.membersubscription.port.out;

import subport.domain.membersubscription.MemberSubscription;

public interface DeleteMemberSubscriptionPort {

	void delete(MemberSubscription memberSubscription);
}
