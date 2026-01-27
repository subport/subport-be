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
import subport.application.subscription.port.out.UploadSubscriptionImagePort;
import subport.domain.member.Member;
import subport.domain.subscription.Subscription;
import subport.domain.subscription.SubscriptionType;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterCustomSubscriptionService implements RegisterCustomSubscriptionUseCase {

	private final SaveSubscriptionPort saveSubscriptionPort;
	private final LoadMemberPort loadMemberPort;
	private final UploadSubscriptionImagePort uploadSubscriptionImagePort;

	@Override
	public RegisterCustomSubscriptionResponse register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	) {
		String logoImageUrl = null; // 기본 이미지 디자인 완성되면 대체
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
