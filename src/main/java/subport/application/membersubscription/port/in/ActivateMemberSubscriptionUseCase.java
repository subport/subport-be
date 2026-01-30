package subport.application.membersubscription.port.in;

import java.time.LocalDateTime;

import subport.application.membersubscription.port.in.dto.ActivateMemberSubscriptionRequest;

public interface ActivateMemberSubscriptionUseCase {

	void activate(
		Long memberId,
		ActivateMemberSubscriptionRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	);
}
