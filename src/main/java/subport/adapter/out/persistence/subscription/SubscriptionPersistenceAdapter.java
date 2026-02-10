package subport.adapter.out.persistence.subscription;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminSubscriptionPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeleteSubscriptionPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.domain.subscription.Subscription;

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
	public List<Subscription> searchSubscriptions(Long memberId, String name) {
		return subscriptionRepository.findByMemberIdAndNameContaining(memberId, name);
	}

	@Override
	public void delete(Subscription subscription) {
		subscriptionRepository.delete(subscription);
	}

	@Override
	public List<Subscription> loadSubscriptions() {
		return subscriptionRepository.findAll();
	}
}
