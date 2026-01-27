package subport.application.member.port.in.dto;

import subport.domain.member.Member;

public record GetMemberResponse(
	Long id,
	String nickname,
	String email
) {

	public static GetMemberResponse from(Member member) {
		return new GetMemberResponse(
			member.getId(),
			member.getNickname(),
			member.getEmail()
		);
	}
}
