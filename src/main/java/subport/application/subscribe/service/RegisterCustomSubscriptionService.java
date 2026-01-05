package subport.application.subscribe.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionUseCase;
import subport.application.subscribe.port.out.SaveCustomSubscriptionPort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@RequiredArgsConstructor
public class RegisterCustomSubscriptionService implements RegisterCustomSubscriptionUseCase {

	private final SaveCustomSubscriptionPort saveCustomSubscriptionPort;

	@Override
	public void register(Long memberId, RegisterCustomSubscriptionRequest request) {
		Subscription subscription = Subscription.withoutId(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			request.logoImageUrl(),
			null,
			memberId
		);
		saveCustomSubscriptionPort.save(subscription);
	}
}
