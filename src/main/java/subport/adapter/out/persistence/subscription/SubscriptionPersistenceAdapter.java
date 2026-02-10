package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminSubscriptionPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeleteSubscriptionPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Component
@RequiredArgsConstructor
public class SubscriptionPersistenceAdapter implements
	SaveSubscriptionPort,
	LoadSubscriptionPort,
	DeleteSubscriptionPort,
	AdminSubscriptionPort {

	private final SpringDataSubscriptionRepository subscriptionRepository;

	@Override
	public Long save(Subscription subscription) {
		return subscriptionRepository.save(subscription).getId();
	}

	@Override
	public Subscription loadSubscription(Long subscriptionId) {
		return subscriptionRepository.findById(subscriptionId)
			.orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
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
	public List<Subscription> searchSubscriptions(Long memberId, String name) {
		return subscriptionRepository.findByMemberIdAndNameContaining(memberId, name);
	}

	@Override
	public void delete(Subscription subscription) {
		subscriptionRepository.delete(subscription);
	}
}
