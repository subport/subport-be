package subport.application.subscription.port.in;

public interface RegisterCustomSubscriptionPlanUseCase {

	void register(
		Long memberId,
		RegisterCustomSubscriptionPlanRequest request,
		Long subscriptionId
	);
}
