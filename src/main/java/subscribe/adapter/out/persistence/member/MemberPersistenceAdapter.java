package subscribe.adapter.out.persistence.member;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subscribe.application.member.port.out.SaveMemberPort;
import subscribe.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
	SaveMemberPort {

	private final SpringDataMemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public void saveMember(Member member) {
		if (!memberRepository.existsByProviderId(member.getProviderId())) {
			memberRepository.save(memberMapper.toJpaEntity(member));
		}
	}
}
