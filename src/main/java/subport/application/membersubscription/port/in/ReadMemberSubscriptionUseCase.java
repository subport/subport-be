package subport.application.membersubscription.port.in;

import subport.application.membersubscription.port.in.dto.ReadMemberSubscriptionResponse;

public interface ReadMemberSubscriptionUseCase {

	ReadMemberSubscriptionResponse read(Long memberId, Long memberSubscriptionId);
}
