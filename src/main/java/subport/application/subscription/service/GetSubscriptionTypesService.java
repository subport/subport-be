package subport.application.subscription.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import subport.application.subscription.port.in.GetSubscriptionTypesUseCase;
import subport.domain.subscription.SubscriptionType;

@Service
public class GetSubscriptionTypesService implements GetSubscriptionTypesUseCase {

	@Override
	public List<String> get() {
		SubscriptionType[] subscriptionTypes = SubscriptionType.values();

		return Arrays.stream(subscriptionTypes)
			.map(SubscriptionType::getDisplayName)
			.sorted()
			.toList();
	}
}
