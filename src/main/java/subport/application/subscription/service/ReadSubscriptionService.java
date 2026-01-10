package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.subscription.port.in.ReadSubscriptionUseCase;
import subport.application.subscription.port.out.ListSubscriptionsResponse;
import subport.application.subscription.port.out.LoadSubscriptionPort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadSubscriptionService implements ReadSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public ListSubscriptionsResponse list(Long memberId) {
		return ListSubscriptionsResponse.fromDomains(loadSubscriptionPort.loadByMemberId(memberId));
	}
}
