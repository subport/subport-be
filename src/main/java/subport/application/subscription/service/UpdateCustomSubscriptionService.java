package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.subscription.port.in.UpdateCustomSubscriptionRequest;
import subport.application.subscription.port.in.UpdateCustomSubscriptionUseCase;
import subport.application.subscription.port.out.LoadSubscriptionPort;
import subport.application.subscription.port.out.UpdateSubscriptionPort;
import subport.application.subscription.port.out.UploadSubscriptionImagePort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@RequiredArgsConstructor
public class UpdateCustomSubscriptionService implements UpdateCustomSubscriptionUseCase {

	private final LoadSubscriptionPort loadSubscriptionPort;
	private final UploadSubscriptionImagePort uploadSubscriptionImagePort;
	private final UpdateSubscriptionPort updateSubscriptionPort;

	@Transactional
	@Override
	public void update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile image
	) {
		Subscription subscription = loadSubscriptionPort.load(subscriptionId);

		if (subscription.isSystemProvided()) {
			throw new CustomException(ErrorCode.SYSTEM_SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		if (!memberId.equals(subscription.getMemberId())) {
			throw new CustomException(ErrorCode.SUBSCRIPTION_WRITE_FORBIDDEN);
		}

		String logoImageUrl = null; // 기본 이미지 디자인 완성되면 대체
		if (image != null) {
			logoImageUrl = uploadSubscriptionImagePort.upload(image);
		}

		subscription.update(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			logoImageUrl,
			null
		);

		updateSubscriptionPort.update(subscription);
	}
}
