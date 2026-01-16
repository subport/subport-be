package subport.application.membersubscription.port.in;

import org.springframework.stereotype.Service;

import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;

@Service
public interface RegisterMemberSubscriptionUseCase {

	RegisterMemberSubscriptionResponse register(
		Long memberId,
		RegisterMemberSubscriptionRequest request
	);
}
