package subport.application.member.port.in.dto;

import subport.domain.member.Member;

public record LoginMemberInfo(
	String providerId,
	String nickname,
	String email
) {

	public Member toDomain() {
		return Member.withoutId(
			providerId,
			nickname,
			email
		);
	}
}
