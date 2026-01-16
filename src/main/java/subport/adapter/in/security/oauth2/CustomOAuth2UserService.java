package subport.adapter.in.security.oauth2;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.SyncMemberUseCase;
import subport.application.member.port.in.dto.LoginMemberInfo;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final SyncMemberUseCase syncMemberUseCase;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		KakaoMemberInfo kakaoMemberInfo = KakaoMemberInfo.from(oAuth2User.getAttributes());
		LoginMemberInfo loginMemberInfo = kakaoMemberInfo.toLoginMemberInfo();

		Long memberId = syncMemberUseCase.sync(loginMemberInfo);

		return new CustomOAuth2User(memberId);
	}
}
