package subport.adapter.out.persistence.member;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminMemberPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.member.port.out.SaveMemberPort;
import subport.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
	LoadMemberPort,
	SaveMemberPort,
	AdminMemberPort {

	private final SpringDataMemberRepository memberRepository;

	@Override
	public Member load(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Member load(String providerId) {
		return memberRepository.findByProviderId(providerId)
			.orElse(null);
	}

	@Override
	public Long save(Member member) {
		return memberRepository.save(member).getId();
	}

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
