package subport.application.membersubscription.port.in;

import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionDutchPayRequest;

public interface UpdateMemberSubscriptionDutchPayUseCase {

	void updateDutchPay(
		Long memberId,
		UpdateMemberSubscriptionDutchPayRequest request,
		Long memberSubscriptionId
	);
}
