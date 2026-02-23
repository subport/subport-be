package subport.api.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.subscription.port.in.SubscriptionQueryUseCase;
import subport.api.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.api.application.subscription.port.in.dto.GetSubscriptionsResponse;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.subscription.Subscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscriptionQueryService implements SubscriptionQueryUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public GetSubscriptionResponse getSubscription(Long memberId, Long subscriptionId) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		if (!subscription.isSystemProvided() && !subscription.getMember().getId().equals(memberId)) {
			throw new CustomException(ApiErrorCode.SUBSCRIPTION_READ_FORBIDDEN);
		}

		return GetSubscriptionResponse.from(subscription);
	}

	@Override
	public GetSubscriptionsResponse searchSubscriptions(Long memberId, String name) {
		return GetSubscriptionsResponse.from(loadSubscriptionPort.searchSubscriptions(memberId, name));
	}
}
