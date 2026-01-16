package subport.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.dto.OAuth2UserInfo;
import subport.application.member.port.in.OAuth2UserSyncUseCase;
import subport.application.member.port.out.SyncMemberPort;
import subport.domain.member.Member;

@Service
@RequiredArgsConstructor
public class OAuth2UserSyncService implements OAuth2UserSyncUseCase {

	private final SyncMemberPort syncMemberPort;

	@Transactional
	@Override
	public Long syncOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
		Member member = oAuth2UserInfo.toMember();

		return syncMemberPort.syncMember(member);
	}
}
