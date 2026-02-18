package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.SyncMemberUseCase;
import subport.application.member.port.in.dto.LoginMemberInfo;
import subport.application.member.port.in.dto.SyncMemberInfo;
import subport.application.member.port.out.LoadMemberPort;
import subport.application.member.port.out.SaveMemberPort;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class SyncMemberService implements SyncMemberUseCase {

	private final LoadMemberPort loadMemberPort;
	private final SaveMemberPort saveMemberPort;

	@Override
	public SyncMemberInfo sync(LoginMemberInfo loginMemberInfo) {
		Member member = loadMemberPort.load(loginMemberInfo.providerId());

		if (member == null) {
			Long newMemberId = saveMemberPort.save(loginMemberInfo.toMember());
			return new SyncMemberInfo(newMemberId, true);
		}

		return new SyncMemberInfo(member.getId(), false);
	}
}
