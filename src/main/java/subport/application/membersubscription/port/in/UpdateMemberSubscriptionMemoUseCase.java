package subport.application.membersubscription.port.in;

public interface UpdateMemberSubscriptionMemoUseCase {

	void updateMemo(
		Long memberId,
		UpdateMemberSubscriptionMemoRequest request,
		Long memberSubscriptionId
	);
}
