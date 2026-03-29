package subport.api.application.membersubscription.port.out;

import java.time.LocalDate;
import java.util.List;

import subport.domain.membersubscription.MemberSubscription;

public interface LoadMemberSubscriptionPort {

	MemberSubscription loadMemberSubscription(Long memberSubscriptionId);

	List<MemberSubscription> loadMemberSubscriptions(Long memberId);

	List<MemberSubscription> loadMemberSubscriptions(
		Long memberId,
		boolean active,
		String sortBy
	);

	List<MemberSubscription> loadMemberSubscriptions(Long memberId, LocalDate start, LocalDate end);

	List<MemberSubscription> loadMemberSubscriptions(Long memberId, LocalDate targetDate);

	List<MemberSubscription> loadMemberSubscriptionsForMonth(Long memberId, LocalDate start, LocalDate end);

	boolean existsMemberSubscriptionBySubscriptionId(Long subscriptionId);

	boolean existsMemberSubscriptionByPlanId(Long planId);
}
