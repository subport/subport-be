package subport.application.member.port.in;

import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.UpdateMemberRequest;

public interface UpdateMemberUseCase {

	GetMemberResponse update(Long memberId, UpdateMemberRequest request);
}
