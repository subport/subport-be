package subport.adapter.out.persistence.member;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.member.port.out.SyncMemberPort;
import subport.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
	LoadMemberPort,
	SyncMemberPort {

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
}
