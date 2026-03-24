package subport.admin.adapter.out.persistence.membersubscription;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dashboard.dto.DashboardTopCustomSubscriptionResponse;
import subport.admin.application.dashboard.dto.DashboardTopSubscriptionResponse;
import subport.admin.application.membersubscription.MemberSubscriptionPort;
import subport.admin.application.membersubscription.dto.MemberSubscriptionCount;

@Component
@RequiredArgsConstructor
public class MemberSubscriptionAdapter implements MemberSubscriptionPort {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;

	@Override
	public boolean existsBySubscriptionId(Long subscriptionId) {
		return memberSubscriptionRepository.existsBySubscriptionId(subscriptionId);
	}

	@Override
	public boolean existsByPlanId(Long planId) {
		return memberSubscriptionRepository.existsByPlanId(planId);
	}

	@Override
	public long countActiveMemberSubscriptions() {
		return memberSubscriptionRepository.countActiveMemberSubscriptions();
	}

	@Override
	public long countActiveMemberSubscriptions(LocalDateTime start, LocalDateTime end) {
		return memberSubscriptionRepository.countActiveMemberSubscriptions(start, end);
	}

	@Override
	public List<MemberSubscriptionCount> countActiveMemberSubscriptions(List<Long> memberIds, Boolean systemProvided) {
		return memberSubscriptionRepository.countActiveMemberSubscriptionsByMember(memberIds, systemProvided);
	}

	@Override
	public List<DashboardTopSubscriptionResponse> loadTopSubscriptions(Pageable pageable) {
		return memberSubscriptionRepository.findTopSubscriptions(pageable);
	}

	@Override
	public List<DashboardTopCustomSubscriptionResponse> loadTopCustomSubscriptions(Pageable pageable) {
		return memberSubscriptionRepository.findTopCustomSubscriptions(pageable);
	}
}
