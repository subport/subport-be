package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.DashboardTopCustomSubscriptionResponse;
import subport.admin.application.dto.DashboardTopSubscriptionResponse;
import subport.admin.application.port.AdminMemberSubscriptionPort;
import subport.admin.application.query.MemberSubscriptionCount;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.membersubscription.port.out.DeleteMemberSubscriptionPort;
import subport.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.domain.membersubscription.MemberSubscription;

@Component
@RequiredArgsConstructor
public class MemberSubscriptionPersistenceAdapter implements
	SaveMemberSubscriptionPort,
	LoadMemberSubscriptionPort,
	DeleteMemberSubscriptionPort,
	AdminMemberSubscriptionPort {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;

	@Override
	public MemberSubscription save(MemberSubscription memberSubscription) {
		return memberSubscriptionRepository.save(memberSubscription);
	}

	@Override
	public MemberSubscription loadMemberSubscription(Long memberSubscriptionId) {
		return memberSubscriptionRepository
			.findByIdWithFetch(memberSubscriptionId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_NOT_FOUND));
	}

	@Override
	public List<MemberSubscription> loadMemberSubscriptions(Long memberId) {
		return memberSubscriptionRepository.findByMemberIdAndActiveIsTrue(memberId);
	}

	@Override
	public List<MemberSubscription> loadMemberSubscriptions(
		Long memberId,
		boolean active,
		String sortBy
	) {
		Sort sort = createSort(sortBy);

		return memberSubscriptionRepository.findByMemberIdAndActive(memberId, active, sort);
	}

	@Override
	public List<MemberSubscription> loadMemberSubscriptions(LocalDate currentDate) {
		return memberSubscriptionRepository.findByNextPaymentDateAndActiveTrue(currentDate);
	}

	@Override
	public List<MemberSubscription> loadMemberSubscriptionsForEmail(LocalDate currentDate) {
		return memberSubscriptionRepository.findByPaymentReminderDateAndActiveTrue(currentDate);
	}

	@Override
	public List<MemberSubscription> loadMemberSubscriptions(Long memberId, LocalDate start, LocalDate end) {
		return memberSubscriptionRepository.findByMemberIdAndLastPaymentDateGreaterThanEqualAndLastPaymentDateLessThan(
			memberId,
			start,
			end
		);
	}

	@Override
	public List<MemberSubscription> loadMemberSubscriptions(Long memberId, LocalDate targetDate) {
		return memberSubscriptionRepository.findByMemberIdAndLastPaymentDateAndActiveTrue(memberId, targetDate);
	}

	@Override
	public void delete(MemberSubscription memberSubscription) {
		memberSubscriptionRepository.delete(memberSubscription);
	}

	private Sort createSort(String sortBy) {
		return switch (sortBy) {
			case "nextPaymentDate" -> Sort.by(
				Sort.Direction.ASC,
				"nextPaymentDate",
				"subscription.name"
			);
			case "createdAt" -> Sort.by(
				Sort.Order.desc("createdAt"),
				Sort.Order.asc("subscription.name")
			);
			case "name" -> Sort.by(
				Sort.Direction.ASC,
				"subscription.name"
			);
			default -> Sort.by(
				Sort.Direction.ASC,
				"subscription.type",
				"subscription.name"
			);
		};
	}

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
	public List<MemberSubscriptionCount> countActiveMemberSubscriptions(List<Long> memberIds) {
		return memberSubscriptionRepository.countActiveMemberSubscriptionsByMember(memberIds);
	}

	@Override
	public List<DashboardTopSubscriptionResponse> loadTopSubscriptions() {
		return memberSubscriptionRepository.countActiveMemberSubscriptionsBySubscription(
			PageRequest.of(0, 5)
		);
	}

	@Override
	public List<DashboardTopCustomSubscriptionResponse> loadTopCustomSubscriptions() {
		return memberSubscriptionRepository.countActiveCustomMemberSubscriptions(
			PageRequest.of(0, 5)
		);
	}
}
