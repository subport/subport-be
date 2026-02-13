package subport.adapter.in.security.oauth2;

import java.time.LocalDateTime;
import java.util.Map;

import subport.application.member.port.in.dto.LoginMemberInfo;

public record KakaoMemberInfo(
	Map<String, Object> attributes,
	Map<String, Object> account,
	Map<String, String> profile,
	LocalDateTime loginAt
) {

	public static KakaoMemberInfo from(Map<String, Object> attributes, LocalDateTime now) {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");
		Map<String, String> profile = (Map<String, String>)account.get("profile");

		return new KakaoMemberInfo(
			attributes,
			account,
			profile,
			now
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

	public LoginMemberInfo toLoginMemberInfo() {
		return new LoginMemberInfo(
			getProviderId(),
			getNickname(),
			getEmail(),
			loginAt
		);
	}
}
