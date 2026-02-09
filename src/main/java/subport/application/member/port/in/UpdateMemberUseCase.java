package subport.application.member.port.in;

import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.UpdateMemberRequest;

public interface UpdateMemberUseCase {

	GetMemberResponse updateMember(Long memberId, UpdateMemberRequest request);
}
