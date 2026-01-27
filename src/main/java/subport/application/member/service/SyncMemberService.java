package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.SyncMemberUseCase;
import subport.application.member.port.in.dto.LoginMemberInfo;
import subport.application.member.port.out.SyncMemberPort;
import subport.domain.member.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class SyncMemberService implements SyncMemberUseCase {

	private final SyncMemberPort syncMemberPort;

	@Override
	public Long sync(LoginMemberInfo loginMemberInfo) {
		Member member = loginMemberInfo.toMember();

		return syncMemberPort.sync(member);
	}
}
