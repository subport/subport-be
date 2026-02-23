package subport.admin.application.subscription;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.subscription.dto.SubscriptionResponse;
import subport.admin.application.subscription.dto.SubscriptionsResponse;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubscriptionQueryService {

	private final SubscriptionPort subscriptionPort;

	public SubscriptionsResponse searchSubscriptions(
		String type,
		String name,
		Pageable pageable
	) {
		SubscriptionType subscriptionType = null;
		if (type != null) {
			subscriptionType = SubscriptionType.fromDisplayName(type);
		}

		Page<Subscription> subscriptionsPage = subscriptionPort.searchSubscriptions(
			subscriptionType,
			name,
			pageable
		);

		List<SubscriptionResponse> subscriptions = subscriptionsPage.getContent().stream()
			.map(SubscriptionResponse::from)
			.toList();

		return new SubscriptionsResponse(
			subscriptions,
			subscriptionsPage.getNumber() + 1,
			subscriptionsPage.getTotalElements(),
			subscriptionsPage.getTotalPages()
		);
	}

	public SubscriptionResponse getSubscription(Long subscriptionId) {
		return SubscriptionResponse.from(
			subscriptionPort.loadSubscription(subscriptionId)
		);
	}
}
