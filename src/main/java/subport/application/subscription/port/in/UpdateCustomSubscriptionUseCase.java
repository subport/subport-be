package subport.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

import subport.application.subscription.port.in.dto.UpdateCustomSubscriptionRequest;

public interface UpdateCustomSubscriptionUseCase {

	void update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile image
	);
}
