package subport.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

import subport.application.subscription.port.out.RegisterCustomSubscriptionResponse;

public interface RegisterCustomSubscriptionUseCase {

	RegisterCustomSubscriptionResponse register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	);
}
