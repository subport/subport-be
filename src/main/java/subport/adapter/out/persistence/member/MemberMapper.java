package subport.adapter.out.persistence.member;

import org.springframework.stereotype.Component;

import subport.domain.member.Member;

@Component
public class MemberMapper {

	public MemberJpaEntity toJpaEntity(Member member) {
		return new MemberJpaEntity(
			member.getProviderId(),
			member.getNickname(),
			member.getEmail()
		);
	}
}
