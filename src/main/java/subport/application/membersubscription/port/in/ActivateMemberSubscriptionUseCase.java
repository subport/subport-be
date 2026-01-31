package subport.application.membersubscription.port.in;

import java.time.LocalDateTime;

import subport.application.membersubscription.port.in.dto.ActivateMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;

public interface ActivateMemberSubscriptionUseCase {

	GetMemberSubscriptionResponse activate(
		Long memberId,
		ActivateMemberSubscriptionRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	);
}
