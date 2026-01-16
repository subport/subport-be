package subport.application.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.ReadMemberUseCase;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.member.port.out.ReadMemberResponse;
import subport.domain.member.Member;

@Service
@RequiredArgsConstructor
public class ReadMemberService implements ReadMemberUseCase {

	private final LoadMemberPort loadMemberPort;

	@Override
	public ReadMemberResponse read(Long memberId) {
		Member member = loadMemberPort.load(memberId);

		return ReadMemberResponse.fromDomain(member);
	}
}
