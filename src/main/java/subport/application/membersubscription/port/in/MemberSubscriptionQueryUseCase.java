package subport.application.membersubscription.port.in;

import java.time.LocalDate;

import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionsRequest;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionsResponse;

public interface MemberSubscriptionQueryUseCase {

	GetMemberSubscriptionResponse getMemberSubscription(
		Long memberId,
		Long memberSubscriptionId,
		LocalDate currentDate
	);

	GetMemberSubscriptionsResponse getMemberSubscriptions(
		Long memberId,
		GetMemberSubscriptionsRequest request,
		LocalDate currentDate
	);
}
