package subport.application.membersubscription.port.in;

public interface UpdateMemberSubscriptionPlanUseCase {

	void updatePlan(
		Long memberId,
		UpdateMemberSubscriptionPlanRequest request,
		Long memberSubscriptionId
	);
}
