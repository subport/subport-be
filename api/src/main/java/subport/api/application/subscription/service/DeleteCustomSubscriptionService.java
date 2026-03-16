package subport.api.application.subscription.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.plan.port.out.DeletePlanPort;
import subport.api.application.subscription.SubscriptionPresetImage;
import subport.api.application.subscription.port.in.DeleteCustomSubscriptionUseCase;
import subport.api.application.subscription.port.out.DeleteCustomSubscriptionImagePort;
import subport.api.application.subscription.port.out.DeleteSubscriptionPort;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.subscription.Subscription;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCustomSubscriptionService implements DeleteCustomSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;
	private final DeleteSubscriptionPort deleteSubscriptionPort;
	private final DeletePlanPort deletePlanPort;
	private final DeleteCustomSubscriptionImagePort deleteCustomSubscriptionImagePort;

	@Value("${app.subscription.default-image-url}")
	private String defaultLogoImageUrl;

	@Override
	public void delete(Long memberId, Long subscriptionId) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		if (subscription.isSystemProvided()) {
			throw new CustomException(ApiErrorCode.SYSTEM_SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		if (!memberId.equals(subscription.getMember().getId())) {
			throw new CustomException(ApiErrorCode.SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		deletePlanPort.deleteBySubscriptionId(subscriptionId);

		String logoImageUrl = subscription.getLogoImageUrl();
		if (!defaultLogoImageUrl.equals(logoImageUrl) && !SubscriptionPresetImage.isPresetImage(logoImageUrl)) {
			deleteCustomSubscriptionImagePort.delete(logoImageUrl);
		}

		deleteSubscriptionPort.delete(subscription);
	}
}
