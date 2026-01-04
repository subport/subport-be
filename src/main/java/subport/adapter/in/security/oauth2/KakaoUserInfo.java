package subport.adapter.in.security.oauth2;

import java.util.Map;

import subport.application.member.port.in.OAuth2UserInfo;

public record KakaoUserInfo(
	Map<String, Object> attributes,
	Map<String, Object> account,
	Map<String, String> profile
) {

	public static KakaoUserInfo from(Map<String, Object> attributes) {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, String> profile = (Map<String, String>)account.get("profile");

		return new KakaoUserInfo(
			attributes,
			account,
			profile
		);
	}

	public String getProviderId() {
		return attributes.get("id").toString();
	}

	public String getNickname() {
		return profile.get("nickname");
	}

	public String getEmail() {
		return account.get("email").toString();
	}

	public OAuth2UserInfo toOAuth2UserInfo() {
		return new OAuth2UserInfo(
			getProviderId(),
			getNickname(),
			getEmail()
		);
	}
}
