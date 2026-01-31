package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.UpdateCustomSubscriptionUseCase;
import subport.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.application.subscription.port.in.dto.UpdateCustomSubscriptionRequest;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.UploadSubscriptionImagePort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCustomSubscriptionService implements UpdateCustomSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;
	private final UploadSubscriptionImagePort uploadSubscriptionImagePort;
	private final SubscriptionQueryService subscriptionQueryService;

	@Override
	public GetSubscriptionResponse update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile image
	) {
		Subscription subscription = loadSubscriptionPort.loadSubscription(subscriptionId);

		if (subscription.isSystemProvided()) {
			throw new CustomException(ErrorCode.SYSTEM_SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		if (!memberId.equals(subscription.getMember().getId())) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		String logoImageUrl = subscription.getLogoImageUrl();
		if (image != null) {
			logoImageUrl = uploadSubscriptionImagePort.upload(image);
			// 기존 이미지 버킷에서 삭제 (추가 예정)
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
