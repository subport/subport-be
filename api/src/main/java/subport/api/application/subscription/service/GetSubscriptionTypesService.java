package subport.api.application.subscription.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import subport.api.application.subscription.port.in.GetSubscriptionTypesUseCase;
import subport.domain.subscription.SubscriptionType;

@Service
public class GetSubscriptionTypesService implements GetSubscriptionTypesUseCase {

	@Override
	public List<String> get() {
		SubscriptionType[] subscriptionTypes = SubscriptionType.values();

		return Arrays.stream(subscriptionTypes)
			.sorted(Comparator.comparingInt((SubscriptionType type) -> type == SubscriptionType.ETC ? 1 : 0)
				.thenComparing(SubscriptionType::getDisplayName))
			.map(SubscriptionType::getDisplayName)
			.toList();
	}
}
