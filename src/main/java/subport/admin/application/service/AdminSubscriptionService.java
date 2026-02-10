package subport.admin.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminRegisterSubscriptionRequest;
import subport.admin.application.dto.AdminSubscriptionResponse;
import subport.admin.application.dto.AdminSubscriptionsResponse;
import subport.admin.application.dto.AdminUpdateSubscriptionRequest;
import subport.admin.application.port.AdminMemberSubscriptionPort;
import subport.admin.application.port.AdminSubscriptionPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.out.DeleteSubscriptionPort;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.application.subscription.port.out.UploadSubscriptionImagePort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSubscriptionService {

	private static final String SUBSCRIPTION_DEFAULT_LOGO_IMAGE_URL =
		"https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/axnklumwzgke/b/subpport-bucket/o/subscription_default.png";

	private final AdminSubscriptionPort adminSubscriptionPort;
	private final SaveSubscriptionPort saveSubscriptionPort;
	private final LoadSubscriptionPort loadSubscriptionPort;
	private final UploadSubscriptionImagePort uploadSubscriptionImagePort;
	private final DeleteSubscriptionPort deleteSubscriptionPort;
	private final AdminMemberSubscriptionPort memberSubscriptionPort;

	@Transactional
	public Long registerSubscription(
		AdminRegisterSubscriptionRequest request,
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

		return saveSubscriptionPort.save(subscription);
	}

	public AdminSubscriptionsResponse searchSubscriptions() {
		return new AdminSubscriptionsResponse(
			adminSubscriptionPort.loadSubscriptions().stream()
				.map(AdminSubscriptionResponse::from)
				.toList()
		);
	}

	public AdminSubscriptionResponse getSubscription(Long subscriptionId) {
		return AdminSubscriptionResponse.from(
			loadSubscriptionPort.loadSubscription(subscriptionId)
		);
	}

	@Transactional
	public void updateSubscription(
		Long subscriptionId,
		AdminUpdateSubscriptionRequest request,
		MultipartFile image
	) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

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

	@Transactional
	public void deleteSubscription(Long subscriptionId) {
		if (memberSubscriptionPort.existsBySubscriptionId(subscriptionId)) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_IN_USE);
		}

		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);
		deleteSubscriptionPort.delete(subscription);
	}
}
