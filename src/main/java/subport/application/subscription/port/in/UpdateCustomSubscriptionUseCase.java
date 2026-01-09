package subport.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

public interface UpdateCustomSubscriptionUseCase {

	void update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile image
	);
}
