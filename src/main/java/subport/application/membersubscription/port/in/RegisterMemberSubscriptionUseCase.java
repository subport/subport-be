package subport.application.membersubscription.port.in;

import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;

public interface RegisterMemberSubscriptionUseCase {

	RegisterMemberSubscriptionResponse register(
		Long memberId,
		RegisterMemberSubscriptionRequest request
	);
}
