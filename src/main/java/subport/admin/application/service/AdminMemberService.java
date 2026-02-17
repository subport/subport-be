package subport.admin.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminMemberResponse;
import subport.admin.application.dto.AdminMembersResponse;
import subport.admin.application.port.AdminMemberPort;
import subport.admin.application.port.AdminMemberSubscriptionPort;
import subport.admin.application.query.MemberSubscriptionCount;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminMemberService {

	private final AdminMemberPort memberPort;
	private final AdminMemberSubscriptionPort memberSubscriptionPort;

	public AdminMembersResponse searchMembers(
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

		List<AdminMemberResponse> items = members.stream()
			.map(member -> new AdminMemberResponse(
				member.getId(),
				member.getEmail(),
				member.getNickname(),
				memberSubscriptionCountMap.getOrDefault(member.getId(), 0L),
				CustommemberSubscriptionCountMap.getOrDefault(member.getId(), 0L),
				member.getReminderDaysBefore(),
				member.getLastLoginAt(),
				member.getCreatedAt(),
				member.isDeleted()
			))
			.toList();

		return new AdminMembersResponse(
			items,
			membersPage.getNumber() + 1,
			membersPage.getSize(),
			membersPage.getTotalPages()
		);
	}
}
