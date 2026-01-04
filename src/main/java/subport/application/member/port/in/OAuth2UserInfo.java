package subport.application.member.port.in;

import subport.domain.member.Member;

public record OAuth2UserInfo(
	String providerId,
	String nickname,
	String email
) {

	public Member toMember() {
		return Member.withoutId(
			providerId,
			nickname,
			email
		);
	}
}
