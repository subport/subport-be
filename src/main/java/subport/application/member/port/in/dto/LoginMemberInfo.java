package subport.application.member.port.in.dto;

import subport.domain.member.Member;

public record LoginMemberInfo(
	String providerId,
	String nickname,
	String email
) {

	public Member toMember() {
		return new Member(
			providerId,
			nickname,
			email
		);
	}
}
