package subport.application.member.port.out;

import subport.domain.member.Member;

public record ReadMemberResponse(
	Long id,
	String nickname,
	String email
) {

	public static ReadMemberResponse fromDomain(Member member) {
		return new ReadMemberResponse(
			member.getId(),
			member.getNickname(),
			member.getEmail()
		);
	}
}
