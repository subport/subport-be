package subport.admin.adapter.out.persistence.subscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.application.subscription.SubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements SubscriptionPort {

	private final SpringDataSubscriptionRepository subscriptionRepository;

	@Override
	public Long save(Subscription subscription) {
		return subscriptionRepository.save(subscription).getId();
	}

	@Override
	public Subscription loadSubscription(Long subscriptionId) {
		return subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new CustomException(AdminErrorCode.SUBSCRIPTION_NOT_FOUND));
	}

	@Override
	public Page<Subscription> searchSubscriptions(
		SubscriptionType type,
		String name,
		Pageable pageable
	) {
		return subscriptionRepository.findByTypeContainingAndNameContaining(type, name, pageable);
	}

	@Override
	public void delete(Subscription subscription) {
		subscriptionRepository.delete(subscription);
	}
}
