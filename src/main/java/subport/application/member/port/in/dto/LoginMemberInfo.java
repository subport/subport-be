package subport.application.member.port.in.dto;

import java.time.LocalDateTime;

import subport.domain.member.Member;

public record LoginMemberInfo(
	String providerId,
	String nickname,
	String email,
	LocalDateTime loginAt
) {

	public Member toMember() {
		return new Member(
			providerId,
			nickname,
			email,
			loginAt
		);
	}
}
