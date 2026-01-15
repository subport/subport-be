package subport.application.membersubscription.port.out;

import java.util.List;

import subport.domain.membersubscription.MemberSubscription;

public interface UpdateMemberSubscriptionPort {

	void update(MemberSubscription memberSubscription);

	void update(List<MemberSubscription> memberSubscriptions);
}
