package subport.api.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

import subport.api.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.api.application.subscription.port.in.dto.UpdateCustomSubscriptionRequest;

public interface UpdateCustomSubscriptionUseCase {

	GetSubscriptionResponse update(
		Long memberId,
		Long subscriptionId,
		UpdateCustomSubscriptionRequest request,
		MultipartFile image
	);
}
