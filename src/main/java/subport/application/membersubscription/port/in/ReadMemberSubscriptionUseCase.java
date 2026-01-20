package subport.application.membersubscription.port.in;

import subport.application.membersubscription.port.in.dto.ListMemberSubscriptionsRequest;
import subport.application.membersubscription.port.in.dto.ListMemberSubscriptionsResponse;
import subport.application.membersubscription.port.in.dto.ReadMemberSubscriptionResponse;

public interface ReadMemberSubscriptionUseCase {

	ReadMemberSubscriptionResponse read(Long memberId, Long memberSubscriptionId);

	ListMemberSubscriptionsResponse list(Long memberId, ListMemberSubscriptionsRequest request);
}
