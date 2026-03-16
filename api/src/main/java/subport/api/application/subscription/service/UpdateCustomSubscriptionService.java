package subport.api.application.subscription.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.subscription.port.in.UpdateCustomSubscriptionUseCase;
import subport.api.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.api.application.subscription.port.in.dto.UpdateCustomSubscriptionRequest;
import subport.api.application.subscription.port.out.DeleteCustomSubscriptionImagePort;
import subport.api.application.subscription.port.out.LoadSubscriptionPort;
import subport.api.application.subscription.port.out.UploadCustomSubscriptionImagePort;
import subport.common.exception.CustomException;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCustomSubscriptionService implements UpdateCustomSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;
	private final UploadCustomSubscriptionImagePort uploadSubscriptionImagePort;
	private final SubscriptionQueryService subscriptionQueryService;
	private final DeleteCustomSubscriptionImagePort deleteCustomSubscriptionImagePort;

	@Value("${app.subscription.default-image-url}")
	private String defaultLogoImageUrl;

	@Override
	public GetSubscriptionResponse update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile newImage
	) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		if (subscription.isSystemProvided()) {
			throw new CustomException(ApiErrorCode.SYSTEM_SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		if (!memberId.equals(subscription.getMember().getId())) {
			throw new CustomException(ApiErrorCode.SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		String logoImageUrl = subscription.getLogoImageUrl();
		if (newImage != null) {
			if (!defaultLogoImageUrl.equals(logoImageUrl)) {
				deleteCustomSubscriptionImagePort.delete(logoImageUrl);
			}

			logoImageUrl = uploadSubscriptionImagePort.upload(newImage);
		}

		subscription.update(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			logoImageUrl,
			null
		);

		return subscriptionQueryService.getSubscription(memberId, subscriptionId);
	}
}
