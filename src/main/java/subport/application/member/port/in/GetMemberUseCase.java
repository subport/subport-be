package subport.application.member.port.in;

import subport.application.member.port.in.dto.GetMemberResponse;

public interface GetMemberUseCase {

	GetMemberResponse get(Long memberId);
}
