package subport.adapter.out.persistence.membersubscription;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
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
	DeleteMemberSubscriptionPort {

	private final SpringDataMemberSubscriptionRepository memberSubscriptionRepository;

	@Override
	public Long save(MemberSubscription memberSubscription) {
		return memberSubscriptionRepository.save(memberSubscription).getId();
	}

	@Override
	public MemberSubscription loadMemberSubscription(Long memberSubscriptionId) {
		return memberSubscriptionRepository
			.findByIdWithFetch(memberSubscriptionId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_SUBSCRIPTION_NOT_FOUND));
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
		return memberSubscriptionRepository.findByReminderDateAndActiveTrue(currentDate);
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
			case "type" -> Sort.by(Sort.Direction.ASC, "subscription.type", "subscription.name");
			case "nextPaymentDate" -> Sort.by(Sort.Direction.ASC, "nextPaymentDate", "subscription.name");
			default -> Sort.by(Sort.Direction.ASC, "subscription.name");
		};
	}
}
