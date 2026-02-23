package subport.api.application.membersubscription.port.in;

import java.time.LocalDate;

import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;

public interface DeactivateMemberSubscriptionUseCase {

	GetMemberSubscriptionResponse deactivate(
		Long memberId,
		Long memberSubscriptionId,
		LocalDate currentDate
	);
}
