package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.ReadSubscriptionUseCase;
import subport.application.subscription.port.in.dto.ListSubscriptionsResponse;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.in.dto.ReadSubscriptionResponse;
import subport.domain.subscription.Subscription;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadSubscriptionService implements ReadSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;

	@Override
	public ReadSubscriptionResponse read(Long memberId, Long subscriptionId) {
		Subscription subscription = loadSubscriptionPort.load(subscriptionId);

		if (!subscription.isSystemProvided() && !subscription.getMemberId().equals(memberId)) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_READ_FORBIDDEN); // 시스템 제공이 아닌 구독 서비스는 본인이 생성한 것만 조회 가능
		}

		return ReadSubscriptionResponse.fromDomain(subscription);
	}

	@Override
	public ListSubscriptionsResponse list(Long memberId) {
		return ListSubscriptionsResponse.fromDomains(loadSubscriptionPort.loadByMemberId(memberId));
	}
}
