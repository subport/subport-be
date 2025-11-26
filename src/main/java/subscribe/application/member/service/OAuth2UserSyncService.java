package subscribe.application.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subscribe.application.member.port.in.OAuth2UserInfo;
import subscribe.application.member.port.in.OAuth2UserSyncUseCase;
import subscribe.application.member.port.out.SaveMemberPort;
import subscribe.domain.member.Member;

@Service
@RequiredArgsConstructor
public class OAuth2UserSyncService implements OAuth2UserSyncUseCase {

	private final SaveMemberPort saveMemberPort;

	@Transactional
	@Override
	public void syncOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
		Member member = oAuth2UserInfo.toMember();

		saveMemberPort.saveMember(member);
	}
}
