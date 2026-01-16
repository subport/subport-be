package subport.application.member.port.out;

import subport.domain.member.Member;

public interface LoadMemberPort {

	Member load(Long memberId);
}
