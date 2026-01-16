package subport.application.member.port.in;

import subport.application.member.port.in.dto.ReadMemberResponse;

public interface ReadMemberUseCase {

	ReadMemberResponse read(Long memberId);
}
