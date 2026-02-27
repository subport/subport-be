package subport.api.application.membersubscription.port.out;

import subport.domain.membersubscription.MemberSubscription;

public interface DeleteMemberSubscriptionPort {

	void delete(MemberSubscription memberSubscription);

	void delete(Long memberId);
}
