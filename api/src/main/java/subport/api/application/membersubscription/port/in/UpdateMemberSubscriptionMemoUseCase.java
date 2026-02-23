package subport.api.application.membersubscription.port.in;

import java.time.LocalDate;

import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.api.application.membersubscription.port.in.dto.UpdateMemberSubscriptionMemoRequest;

public interface UpdateMemberSubscriptionMemoUseCase {

	GetMemberSubscriptionResponse updateMemo(
		Long memberId,
		UpdateMemberSubscriptionMemoRequest request,
		Long memberSubscriptionId,
		LocalDate currentDate
	);
}
