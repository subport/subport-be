package subport.application.membersubscription.port.in;

public interface UpdateMemberSubscriptionDutchPayUseCase {

	void updateDutchPay(
		Long memberId,
		UpdateMemberSubscriptionDutchPayRequest request,
		Long memberSubscriptionId
	);
}
