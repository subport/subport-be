package subport.application.membersubscription.port.in;

import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionMemoRequest;

public interface UpdateMemberSubscriptionMemoUseCase {

	void updateMemo(
		Long memberId,
		UpdateMemberSubscriptionMemoRequest request,
		Long memberSubscriptionId
	);
}
