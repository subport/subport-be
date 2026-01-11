package subport.application.membersubscription.port.out;

import subport.domain.membersubscription.MemberSubscription;

public interface LoadMemberSubscriptionPort {

	MemberSubscription load(Long memberSubscriptionId);
}
