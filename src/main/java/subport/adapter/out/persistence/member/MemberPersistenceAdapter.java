package subport.adapter.out.persistence.member;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;
import subport.application.member.port.out.SyncMemberPort;
import subport.application.member.port.out.UpdateMemberPort;
import subport.domain.member.Member;

@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements
	SyncMemberPort,
	UpdateMemberPort {

	private final SpringDataMemberRepository memberRepository;
	private final MemberMapper memberMapper;

	@Override
	public Long syncMember(Member member) {
		return memberRepository.findByProviderId(member.getProviderId())
			.map(MemberJpaEntity::getId)
			.orElseGet(() -> {
				MemberJpaEntity memberEntity = memberMapper.toJpaEntity(member);
				return memberRepository.save(memberEntity).getId();
			});
	}

	@Override
	public void updateMember(Member member) {
		MemberJpaEntity memberEntity = memberRepository.findById(member.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		memberEntity.update(
			member.getNickname(),
			member.getEmail()
		);
	}
}
