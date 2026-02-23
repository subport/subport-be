package subport.api.application.subscription.port.in;

import org.springframework.web.multipart.MultipartFile;

import subport.api.application.subscription.port.in.dto.RegisterCustomSubscriptionRequest;
import subport.api.application.subscription.port.in.dto.RegisterCustomSubscriptionResponse;

public interface RegisterCustomSubscriptionUseCase {

	RegisterCustomSubscriptionResponse register(
		Long memberId,
		RegisterCustomSubscriptionRequest request,
		MultipartFile image
	);
}
