package subport.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface RegisterCustomSubscriptionUseCase {

	void register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	);
}
