package subport.api.application.membersubscription.port.in;

import java.time.LocalDate;

import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.api.application.membersubscription.port.in.dto.GetMemberSubscriptionsResponse;
import subport.api.application.membersubscription.port.in.dto.GetMonthlyExpenseSummaryResponse;

public interface MemberSubscriptionQueryUseCase {

	GetMemberSubscriptionResponse getMemberSubscription(
		Long memberId,
		Long memberSubscriptionId,
		LocalDate currentDate
	);

	GetMemberSubscriptionsResponse getMemberSubscriptions(
		Long memberId,
		boolean active,
		String sortBy,
		LocalDate currentDate
	);

	GetMonthlyExpenseSummaryResponse getMonthlyExpenseSummary(Long memberId, LocalDate currentDate);
}
