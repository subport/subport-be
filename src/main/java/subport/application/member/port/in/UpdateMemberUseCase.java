package subport.application.member.port.in;

import subport.application.member.port.in.dto.UpdateMemberRequest;

public interface UpdateMemberUseCase {

	void update(Long memberId, UpdateMemberRequest request);
}
