package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.DeleteCustomSubscriptionUseCase;
import subport.application.subscription.port.out.DeletePlanPort;
import subport.application.subscription.port.out.DeleteSubscriptionPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.domain.subscription.Subscription;

@Service
@RequiredArgsConstructor
public class DeleteCustomSubscriptionService implements DeleteCustomSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;
	private final DeleteSubscriptionPort deleteSubscriptionPort;
	private final DeletePlanPort deletePlanPort;

	@Transactional
	@Override
	public void delete(Long memberId, Long subscriptionId) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		if (subscription.isSystemProvided()) {
			throw new CustomException(ErrorCode.SYSTEM_SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		if (!memberId.equals(subscription.getMemberId())) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		// 관련 플랜 삭제
		deletePlanPort.deleteBySubscriptionId(subscriptionId);

		deleteSubscriptionPort.delete(subscription.getId());

		// 버킷에서 이미지 삭제 (추가 예정)
	}
}
