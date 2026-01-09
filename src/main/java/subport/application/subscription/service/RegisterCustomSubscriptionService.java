package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.application.subscription.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscription.port.in.RegisterCustomSubscriptionUseCase;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.application.subscription.port.out.UploadSubscriptionImagePort;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@RequiredArgsConstructor
public class RegisterCustomSubscriptionService implements RegisterCustomSubscriptionUseCase {

	private final SaveSubscriptionPort saveSubscriptionPort;
	private final UploadSubscriptionImagePort uploadSubscriptionImagePort;

	@Transactional
	@Override
	public void register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	) {
		String logoImageUrl = null; // 기본 이미지 디자인 완성되면 대체
		if (image != null) {
			logoImageUrl = uploadSubscriptionImagePort.upload(image);
		}

		Subscription subscription = Subscription.withoutId(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			logoImageUrl,
			null,
			false,
			memberId
		);

		saveSubscriptionPort.save(subscription);
	}
}
