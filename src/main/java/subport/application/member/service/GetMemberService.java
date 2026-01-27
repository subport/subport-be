package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.GetMemberUseCase;
import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.out.LoadMemberPort;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetMemberService implements GetMemberUseCase {

	private final LoadMemberPort loadMemberPort;

	@Override
	public GetMemberResponse get(Long memberId) {
		Member member = loadMemberPort.load(memberId);

		return GetMemberResponse.from(member);
	}
}
