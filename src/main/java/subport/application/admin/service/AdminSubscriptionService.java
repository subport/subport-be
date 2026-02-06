package subport.application.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminSubscriptionResponse;
import subport.application.admin.dto.AdminSubscriptionsResponse;
import subport.application.admin.port.AdminSubscriptionPort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSubscriptionService {

	private final AdminSubscriptionPort subscriptionPort;

	public AdminSubscriptionsResponse searchSubscriptions() {
		return new AdminSubscriptionsResponse(
			subscriptionPort.loadSubscriptions().stream()
				.map(AdminSubscriptionResponse::from)
				.toList()
		);
	}
}
