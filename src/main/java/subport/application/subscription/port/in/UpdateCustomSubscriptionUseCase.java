package subport.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

import subport.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.application.subscription.port.in.dto.UpdateCustomSubscriptionRequest;

public interface UpdateCustomSubscriptionUseCase {

	GetSubscriptionResponse update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile image
	);
}
