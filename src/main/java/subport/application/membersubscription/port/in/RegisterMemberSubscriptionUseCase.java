package subport.application.membersubscription.port.in;

import org.springframework.stereotype.Service;

@Service
public interface RegisterMemberSubscriptionUseCase {

	void register(
		Long memberId,
		RegisterMemberSubscriptionRequest request
	);
}
