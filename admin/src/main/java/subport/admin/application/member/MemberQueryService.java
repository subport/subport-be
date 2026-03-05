package subport.admin.application.member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.member.dto.MemberResponse;
import subport.admin.application.member.dto.MembersResponse;
import subport.admin.application.membersubscription.MemberSubscriptionPort;
import subport.admin.application.membersubscription.dto.MemberSubscriptionCount;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

	private final MemberPort memberPort;
	private final MemberSubscriptionPort memberSubscriptionPort;

	public MembersResponse searchMembers(
		Boolean deleted,
		Boolean reminderEnabled,
		String email,
		Pageable pageable
	) {
		Page<Member> membersPage = memberPort.searchMembers(
			deleted,
			reminderEnabled,
			email,
			pageable
		);

		List<Member> members = membersPage.getContent();
		List<Long> memberIds = members.stream()
			.map(Member::getId)
			.toList();

		Map<Long, Long> memberSubscriptionCountMap =
			memberSubscriptionPort.countActiveMemberSubscriptions(memberIds, null).stream()
				.collect(Collectors.toMap(
					MemberSubscriptionCount::memberId,
					MemberSubscriptionCount::memberSubscriptionCount
				));

		Map<Long, Long> CustommemberSubscriptionCountMap =
			memberSubscriptionPort.countActiveMemberSubscriptions(memberIds, false).stream()
				.collect(Collectors.toMap(
					MemberSubscriptionCount::memberId,
					MemberSubscriptionCount::memberSubscriptionCount
				));

		List<MemberResponse> items = members.stream()
			.map(member -> new MemberResponse(
				member.getId(),
				member.getEmail(),
				member.getNickname(),
				memberSubscriptionCountMap.getOrDefault(member.getId(), 0L),
				CustommemberSubscriptionCountMap.getOrDefault(member.getId(), 0L),
				member.isPaymentReminderEnabled(),
				member.getReminderDaysBefore(),
				member.getLastActiveAt(),
				member.getCreatedAt(),
				member.isDeleted()
			))
			.toList();

		return new MembersResponse(
			items,
			membersPage.getNumber() + 1,
			membersPage.getTotalElements(),
			membersPage.getTotalPages()
		);
	}
}
