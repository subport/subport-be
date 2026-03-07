package subport.api.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.membersubscription.port.out.DeleteMemberSubscriptionPort;
import subport.api.application.membersubscription.port.out.LoadMemberSubscriptionPort;
import subport.api.application.membersubscription.port.out.SaveMemberSubscriptionPort;
import subport.common.exception.CustomException;
import subport.domain.membersubscription.MemberSubscription;

@Component
@RequiredArgsConstructor
public class MemberSubscriptionPersistenceAdapter implements
	SaveMemberSubscriptionPort,
	LoadMemberSubscriptionPort,
	DeleteMemberSubscriptionPort {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;

	@Override
	public MemberSubscription save(MemberSubscription memberSubscription) {
		return memberSubscriptionRepository.save(memberSubscription);
	}

	@Override
	public MemberSubscription loadMemberSubscription(Long memberSubscriptionId) {
		return memberSubscriptionRepository
			.findByIdWithFetch(memberSubscriptionId)
			.orElseThrow(() -> new CustomException(ApiErrorCode.MEMBER_SUBSCRIPTION_NOT_FOUND));
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
	public List<MemberSubscription> loadMemberSubscriptions(Long memberId, LocalDate start, LocalDate end) {
		return memberSubscriptionRepository.findByMemberIdAndLastPaymentDateBetween(
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

	@Override
	public void delete(Long memberId) {
		memberSubscriptionRepository.deleteByMemberId(memberId);
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
}
