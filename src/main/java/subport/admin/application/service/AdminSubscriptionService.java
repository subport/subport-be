package subport.admin.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import subport.admin.application.port.UploadAdminSubscriptionImagePort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminSubscriptionService {

	private static final String SUBSCRIPTION_DEFAULT_LOGO_IMAGE_URL =
		"https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/axnklumwzgke/b/subpport-bucket/o/subscription_default.png";

	private final AdminSubscriptionPort adminSubscriptionPort;
	private final UploadAdminSubscriptionImagePort uploadSubscriptionImagePort;
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

		return adminSubscriptionPort.save(subscription);
	}

	public AdminSubscriptionsResponse searchSubscriptions(
		String type,
		String name,
		Pageable pageable
	) {
		SubscriptionType subscriptionType = null;
		if (type != null) {
			subscriptionType = SubscriptionType.fromDisplayName(type);
		}

		Page<Subscription> subscriptionsPage = adminSubscriptionPort.searchSubscriptions(
			subscriptionType,
			name,
			pageable
		);

		List<AdminSubscriptionResponse> subscriptions = subscriptionsPage.getContent().stream()
			.map(AdminSubscriptionResponse::from)
			.toList();

		return new AdminSubscriptionsResponse(
			subscriptions,
			subscriptionsPage.getNumber() + 1,
			subscriptionsPage.getSize(),
			subscriptionsPage.getTotalPages()
		);
	}

	public AdminSubscriptionResponse getSubscription(Long subscriptionId) {
		return AdminSubscriptionResponse.from(
			adminSubscriptionPort.loadSubscription(subscriptionId)
		);
	}

	@Transactional
	public void updateSubscription(
		Long subscriptionId,
		AdminUpdateSubscriptionRequest request,
		MultipartFile image
	) {
		Subscription subscription = adminSubscriptionPort.loadSubscription(subscriptionId);

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

		Subscription subscription = adminSubscriptionPort.loadSubscription(subscriptionId);
		adminSubscriptionPort.delete(subscription);
	}
}
