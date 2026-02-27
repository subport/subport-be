package subport.api.application.subscription.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.api.application.subscription.port.in.RegisterCustomSubscriptionUseCase;
import subport.api.application.subscription.port.in.dto.RegisterCustomSubscriptionRequest;
import subport.api.application.subscription.port.in.dto.RegisterCustomSubscriptionResponse;
import subport.api.application.subscription.port.out.SaveSubscriptionPort;
import subport.api.application.subscription.port.out.UploadCustomSubscriptionImagePort;
import subport.domain.member.Member;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCustomSubscriptionService implements RegisterCustomSubscriptionUseCase {

	private final SaveSubscriptionPort saveSubscriptionPort;
	private final LoadMemberPort loadMemberPort;
	private final UploadCustomSubscriptionImagePort uploadSubscriptionImagePort;

	@Value("${subscription.default-logo-url}")
	private String defaultLogoImageUrl;

	@Override
	public RegisterCustomSubscriptionResponse register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	) {
		String logoImageUrl = defaultLogoImageUrl;
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
