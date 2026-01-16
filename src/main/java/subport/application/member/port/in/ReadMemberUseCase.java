package subport.application.member.port.in;

import subport.application.member.port.out.ReadMemberResponse;

public interface ReadMemberUseCase {

	ReadMemberResponse read(Long memberId);
}
