package subport.application.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminSubscriptionResponse;
import subport.application.admin.dto.AdminSubscriptionsResponse;
import subport.application.admin.port.AdminSubscriptionPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSubscriptionService {

	private final AdminSubscriptionPort adminSubscriptionPort;
	private final LoadSubscriptionPort loadSubscriptionPort;

	public AdminSubscriptionsResponse searchSubscriptions() {
		return new AdminSubscriptionsResponse(
			adminSubscriptionPort.loadSubscriptions().stream()
				.map(AdminSubscriptionResponse::from)
				.toList()
		);
	}

	public AdminSubscriptionResponse getSubscription(Long subscriptionId) {
		return AdminSubscriptionResponse.from(
			loadSubscriptionPort.loadSubscription(subscriptionId)
		);
	}
}
