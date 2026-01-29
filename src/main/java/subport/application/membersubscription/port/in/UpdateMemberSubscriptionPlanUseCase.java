package subport.application.membersubscription.port.in;

import java.time.LocalDateTime;

import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionPlanRequest;

public interface UpdateMemberSubscriptionPlanUseCase {

	void updatePlan(
		Long memberId,
		UpdateMemberSubscriptionPlanRequest request,
		Long memberSubscriptionId,
		LocalDateTime currentDateTime
	);
}
