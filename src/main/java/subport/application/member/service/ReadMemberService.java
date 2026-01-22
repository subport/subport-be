package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.ReadMemberUseCase;
import subport.application.member.port.in.dto.ReadMemberResponse;
import subport.application.member.port.out.LoadMemberPort;
import subport.domain.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadMemberService implements ReadMemberUseCase {

	private final LoadMemberPort loadMemberPort;

	@Override
	public ReadMemberResponse read(Long memberId) {
		Member member = loadMemberPort.load(memberId);

		return ReadMemberResponse.fromDomain(member);
	}
}
