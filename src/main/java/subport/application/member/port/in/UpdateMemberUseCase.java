package subport.application.member.port.in;

public interface UpdateMemberUseCase {

	void update(Long memberId, UpdateMemberRequest request);
}
