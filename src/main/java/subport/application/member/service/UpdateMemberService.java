package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.UpdateMemberUseCase;
import subport.application.member.port.in.dto.UpdateMemberRequest;
import subport.application.member.port.out.LoadMemberPort;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService implements UpdateMemberUseCase {

	private final LoadMemberPort loadMemberPort;

	@Override
	public void update(Long memberId, UpdateMemberRequest request) {
		Member member = loadMemberPort.load(memberId);

		member.update(
			request.nickname(),
			request.email()
		);
	}
}
