package subport.api.adapter.out.persistence.member;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.exception.ApiErrorCode;
import subport.api.application.member.port.out.LoadMemberPort;
import subport.api.application.member.port.out.SaveMemberPort;
import subport.common.exception.CustomException;
import subport.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
	SaveMemberPort,
	LoadMemberPort {

	private final SpringDataMemberRepository memberRepository;

	@Override
	public Long save(Member member) {
		return memberRepository.save(member).getId();
	}

	@Override
	public Member load(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ApiErrorCode.MEMBER_NOT_FOUND));
	}

	@Override
	public Member load(String providerId) {
		return memberRepository.findByProviderId(providerId)
			.orElse(null);
	}
}
