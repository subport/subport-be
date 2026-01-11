package subport.application.membersubscription.port.in;

import org.springframework.stereotype.Service;

import subport.application.membersubscription.port.out.RegisterMemberSubscriptionResponse;

@Service
public interface RegisterMemberSubscriptionUseCase {

	RegisterMemberSubscriptionResponse register(
		Long memberId,
		RegisterMemberSubscriptionRequest request
	);
}
