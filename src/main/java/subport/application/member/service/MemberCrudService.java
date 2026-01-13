package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.UpdateMemberRequest;
import subport.application.member.port.in.UpdateMemberUseCase;
import subport.application.member.port.out.UpdateMemberPort;
import subport.domain.member.Member;

@Service
@RequiredArgsConstructor
public class MemberCrudService implements UpdateMemberUseCase {

	private final UpdateMemberPort updateMemberPort;

	@Transactional
	@Override
	public void updateMember(Long memberId, UpdateMemberRequest request) {
		Member member = Member.withId(
			memberId,
			null,
			request.nickname(),
			request.email()
		);
		updateMemberPort.updateMember(member);
	}
}
