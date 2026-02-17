package subport.adapter.out.persistence.member;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.AdminMemberPort;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.member.port.out.SyncMemberPort;
import subport.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
	LoadMemberPort,
	SyncMemberPort,
	AdminMemberPort {

	private final SpringDataMemberRepository memberRepository;

	@Override
	public Member load(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Long sync(Member member) {
		return memberRepository.findByProviderId(member.getProviderId())
			.map(Member::getId)
			.orElseGet(() -> memberRepository.save(member).getId());
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
}
