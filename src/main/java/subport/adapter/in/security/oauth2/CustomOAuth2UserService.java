package subport.adapter.in.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.OAuth2UserInfo;
import subport.application.member.port.in.OAuth2UserSyncUseCase;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final OAuth2UserSyncUseCase oAuth2UserSyncUseCase;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		KakaoUserInfo kakaoUserInfo = KakaoUserInfo.from(oAuth2User.getAttributes());
		OAuth2UserInfo oAuth2UserInfo = kakaoUserInfo.toOAuth2UserInfo();

		Long memberId = oAuth2UserSyncUseCase.syncOAuth2User(oAuth2UserInfo);

		return new CustomOAuth2User(memberId);
	}
}
