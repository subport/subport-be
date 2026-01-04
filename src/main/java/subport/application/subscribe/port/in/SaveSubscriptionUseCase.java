package subport.application.subscribe.port.in;

public interface SaveSubscriptionUseCase {

	void saveSubscription(Long memberId, SaveSubscriptionRequest request);
}
