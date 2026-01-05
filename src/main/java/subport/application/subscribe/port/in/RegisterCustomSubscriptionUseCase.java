package subport.application.subscribe.port.in;

public interface RegisterCustomSubscriptionUseCase {

	void register(Long memberId, RegisterCustomSubscriptionRequest request);
}
