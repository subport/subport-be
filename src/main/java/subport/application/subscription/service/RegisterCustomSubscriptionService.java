package subport.application.subscription.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.subscription.port.in.RegisterCustomSubscriptionUseCase;
import subport.application.subscription.port.in.dto.RegisterCustomSubscriptionRequest;
import subport.application.subscription.port.in.dto.RegisterCustomSubscriptionResponse;
import subport.application.subscription.port.out.SaveSubscriptionPort;
import subport.application.subscription.port.out.UploadCustomSubscriptionImagePort;
import subport.domain.member.Member;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCustomSubscriptionService implements RegisterCustomSubscriptionUseCase {

	private static final String SUBSCRIPTION_DEFAULT_LOGO_IMAGE_URL =
		"https://objectstorage.ap-chuncheon-1.oraclecloud.com/n/axnklumwzgke/b/subpport-bucket/o/subscription_default.png";

	private final SaveSubscriptionPort saveSubscriptionPort;
	private final LoadMemberPort loadMemberPort;
	private final UploadCustomSubscriptionImagePort uploadSubscriptionImagePort;

	@Override
	public RegisterCustomSubscriptionResponse register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	) {
		String logoImageUrl = SUBSCRIPTION_DEFAULT_LOGO_IMAGE_URL;
		if (image != null) {
			logoImageUrl = uploadSubscriptionImagePort.upload(image);
		}

		Member member = loadMemberPort.load(memberId);

		Subscription subscription = new Subscription(
			request.name(),
			SubscriptionType.fromDisplayName(request.type()),
			logoImageUrl,
			null,
			false,
			member
		);

		return new RegisterCustomSubscriptionResponse(
			saveSubscriptionPort.save(subscription)
		);
	}
}
