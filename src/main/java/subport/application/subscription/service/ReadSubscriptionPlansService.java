package subport.application.subscription.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.subscription.port.in.ReadSubscriptionPlansUseCase;
import subport.application.subscription.port.out.ListSubscriptionPlansResponse;
import subport.application.subscription.port.out.LoadSubscriptionPlanPort;

@Service
@RequiredArgsConstructor
public class ReadSubscriptionPlansService implements ReadSubscriptionPlansUseCase {

	private final LoadSubscriptionPlanPort loadSubscriptionPlanPort;

	@Override
	public ListSubscriptionPlansResponse list(Long memberId, Long subscriptionId) {
		return ListSubscriptionPlansResponse.fromDomains(
			loadSubscriptionPlanPort.loadByMemberIdAndSubscriptionId(memberId, subscriptionId));
	}
}
