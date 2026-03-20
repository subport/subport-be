package subport.api.application.member.port.in.dto;

import java.time.LocalDateTime;

import subport.domain.member.Member;

public record LoginMemberInfo(
	String providerId,
	String nickname,
	String email,
	LocalDateTime loginAt
) {

	public Member toMember() {
		return Member.ofMember(
			providerId,
			nickname,
			email,
			loginAt
		);
	}
}
