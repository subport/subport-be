package subport.admin.application.subscription;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.application.membersubscription.MemberSubscriptionPort;
import subport.admin.application.subscription.dto.RegisterSubscriptionRequest;
import subport.admin.application.subscription.dto.UpdateSubscriptionRequest;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionCommandService {

	private static final String SUBSCRIPTION_DEFAULT_LOGO_IMAGE_URL =
		"https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/axnklumwzgke/b/subpport-bucket/o/subscription_default.png";

	private final SubscriptionPort subscriptionPort;
	private final UploadSubscriptionImagePort uploadSubscriptionImagePort;

	private final MemberSubscriptionPort memberSubscriptionPort;

	public Long registerSubscription(
		RegisterSubscriptionRequest request,
		MultipartFile image
	) {
		String logoImageUrl = SUBSCRIPTION_DEFAULT_LOGO_IMAGE_URL;
		if (image != null) {
			logoImageUrl = uploadSubscriptionImagePort.upload(image);
		}

		Subscription subscription = new Subscription(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			logoImageUrl,
			request.planUrl(),
			true,
			null
		);

		return subscriptionPort.save(subscription);
	}

	public void updateSubscription(
		Long subscriptionId,
		UpdateSubscriptionRequest request,
		MultipartFile image
	) {
		Subscription subscription = subscriptionPort.loadSubscription(subscriptionId);

		String logoImageUrl = subscription.getLogoImageUrl();
		if (image != null) {
			logoImageUrl = uploadSubscriptionImagePort.upload(image);
		}

		subscription.update(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			logoImageUrl,
			request.planUrl()
		);
	}

	public void deleteSubscription(Long subscriptionId) {
		if (memberSubscriptionPort.existsBySubscriptionId(subscriptionId)) {
			throw new subport.common.exception.CustomException(AdminErrorCode.SUBSCRIPTION_IN_USE);
		}

		Subscription subscription = subscriptionPort.loadSubscription(subscriptionId);
		subscriptionPort.delete(subscription);
	}
}
