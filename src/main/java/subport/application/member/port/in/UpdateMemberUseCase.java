package subport.application.member.port.in;

public interface UpdateMemberUseCase {

	void updateMember(Long memberId, UpdateMemberRequest request);
}
