package subport.application.membersubscription.port.in;

import java.time.LocalDate;

import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionDutchPayRequest;

public interface UpdateMemberSubscriptionDutchPayUseCase {

	GetMemberSubscriptionResponse updateDutchPay(
		Long memberId,
		UpdateMemberSubscriptionDutchPayRequest request,
		Long memberSubscriptionId,
		LocalDate currentDate
	);
}
