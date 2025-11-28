package subscribe.application.subscribe.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subscribe.application.subscribe.port.in.SaveSubscriptionRequest;
import subscribe.application.subscribe.port.in.SaveSubscriptionUseCase;
import subscribe.application.subscribe.port.out.SaveSubscriptionPort;
import subscribe.domain.subscription.Subscription;

@Service
@RequiredArgsConstructor
public class SubscriptionCrudService implements SaveSubscriptionUseCase {

	private final SaveSubscriptionPort saveSubscriptionPort;

	@Override
	public void saveSubscription(Long memberId, SaveSubscriptionRequest request) {
		Subscription subscription = Subscription.withoutId(
			request.name(),
			request.typeId(),
			request.planId(),
			request.headCount(),
			request.startAt(),
			request.endAt(),
			request.memo(),
			memberId
		);
		saveSubscriptionPort.saveSubscription(subscription);
	}
}
