package subport.admin.adapter.out.persistence.member;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.member.MemberPort;
import subport.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPort {

	private final SpringDataMemberRepository memberRepository;

	@Override
	public List<Member> loadMembers(LocalDateTime start, LocalDateTime end) {
		return memberRepository.getMembers(start, end);
	}

	@Override
	public List<Member> loadLatestMembers() {
		return memberRepository.findTop4ByOrderByCreatedAtDescIdAsc();
	}

	@Override
	public long countMembers() {
		return memberRepository.countMembers();
	}

	@Override
	public long countMembers(LocalDateTime start, LocalDateTime end) {
		return memberRepository.countMembers(start, end);
	}

	@Override
	public long countActiveMembers(LocalDateTime start, LocalDateTime end) {
		return memberRepository.countActiveMembers(start, end);
	}

	@Override
	public Page<Member> searchMembers(
		Boolean deleted,
		Boolean reminderEnabled,
		String email,
		Pageable pageable
	) {
		return memberRepository.findByDeletedAndReminderEnabledAndEmailContaining(
			deleted,
			reminderEnabled,
			email,
			pageable
		);
	}
}
