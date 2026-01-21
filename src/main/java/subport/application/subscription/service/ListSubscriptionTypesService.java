package subport.application.subscription.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import subport.application.subscription.port.in.ListSubscriptionTypesUseCase;
import subport.domain.subscription.SubscriptionType;

@Service
public class ListSubscriptionTypesService implements ListSubscriptionTypesUseCase {

	@Override
	public List<String> list() {
		SubscriptionType[] subscriptionTypes = SubscriptionType.values();

		return Arrays.stream(subscriptionTypes)
			.map(SubscriptionType::getDisplayName)
			.sorted()
			.toList();
	}
}
