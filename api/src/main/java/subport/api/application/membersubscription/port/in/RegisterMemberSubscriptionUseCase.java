package subport.api.application.membersubscription.port.in;

import java.time.LocalDateTime;

import subport.api.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.api.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;

public interface RegisterMemberSubscriptionUseCase {

	RegisterMemberSubscriptionResponse register(
		Long memberId,
		RegisterMemberSubscriptionRequest request,
		LocalDateTime currentDateTime
	);
}
