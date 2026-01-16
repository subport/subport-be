package subport.application.member.port.in;

import subport.application.member.port.in.dto.OAuth2UserInfo;

public interface OAuth2UserSyncUseCase {

	Long syncOAuth2User(OAuth2UserInfo oAuth2UserInfo);
}
