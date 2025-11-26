package subscribe.application.member.port.out;

import subscribe.domain.member.Member;

public interface SaveMemberPort {

	void saveMember(Member member);
}
