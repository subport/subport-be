package subport.api.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.subscription.port.out.DeleteSubscriptionPort;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.api.application.subscription.port.out.SaveSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.subscription.Subscription;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements
	SaveSubscriptionPort,
	LoadSubscriptionPort,
	DeleteSubscriptionPort {

	private final SpringDataSubscriptionRepository subscriptionRepository;

	@Override
	public Long save(Subscription subscription) {
		return subscriptionRepository.save(subscription).getId();
	}

	@Override
	public Subscription loadSubscription(Long subscriptionId) {
		return subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new CustomException(ApiErrorCode.SUBSCRIPTION_NOT_FOUND));
	}

	@Override
	public List<Subscription> searchSubscriptions(Long memberId, String name) {
		return subscriptionRepository.findByMemberIdAndNameContaining(memberId, name);
	}

	@Override
	public void delete(Subscription subscription) {
		subscriptionRepository.delete(subscription);
	}
}
