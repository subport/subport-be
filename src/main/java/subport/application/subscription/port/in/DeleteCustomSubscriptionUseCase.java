package subport.application.subscription.port.in;

public interface DeleteCustomSubscriptionUseCase {

	void delete(Long memberId, Long subscriptionId);
}
