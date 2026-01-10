package subport.application.subscription.port.in;

public interface UpdateCustomPlanUseCase {

	void update(
		Long memberId,
		UpdateCustomPlanRequest request,
		Long subscriptionPlanId
	);
}
