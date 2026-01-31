package subport.application.membersubscription.port.in;

import java.time.LocalDateTime;

import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionPlanRequest;

public interface UpdateMemberSubscriptionPlanUseCase {

	GetMemberSubscriptionResponse updatePlan(
		Long memberId,
		UpdateMemberSubscriptionPlanRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	);
}
