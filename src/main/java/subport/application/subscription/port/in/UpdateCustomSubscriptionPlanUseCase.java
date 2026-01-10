package subport.application.subscription.port.in;

public interface UpdateCustomSubscriptionPlanUseCase {

	void update(
		Long memberId,
		UpdateCustomSubscriptionPlanRequest request,
		Long subscriptionPlanId
	);
}
