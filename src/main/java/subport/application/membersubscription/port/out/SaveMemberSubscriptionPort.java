package subport.application.membersubscription.port.out;

import subport.domain.membersubscription.MemberSubscription;

public interface SaveMemberSubscriptionPort {

	void save(MemberSubscription memberSubscription);
}
