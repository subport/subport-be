package subport.api.application.auth.port.in.dto;

import subport.domain.member.MemberRole;

public record AuthMemberInfo(
	Long subjectId,
	MemberRole role
) {
}
