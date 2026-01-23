package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.SubscriptionQueryUseCase;
import subport.application.subscription.port.in.dto.ListSubscriptionsResponse;
import subport.application.subscription.port.in.dto.ReadSubscriptionResponse;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.domain.subscription.Subscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscriptionQueryService implements SubscriptionQueryUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public ReadSubscriptionResponse getSubscription(Long memberId, Long subscriptionId) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		if (!subscription.isSystemProvided() && !subscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_READ_FORBIDDEN);
		}

		return ReadSubscriptionResponse.fromDomain(subscription);
	}

	@Override
	public ListSubscriptionsResponse searchSubscriptions(Long memberId, String name) {
		return ListSubscriptionsResponse.fromDomains(loadSubscriptionPort.searchSubscriptions(memberId, name));
	}
}
