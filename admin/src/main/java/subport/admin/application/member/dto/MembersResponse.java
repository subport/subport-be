package subport.admin.application.member.dto;

import java.util.List;

public record MembersResponse(
	List<MemberResponse> members,
	int currentPage,
	long totalElements,
	int totalPages
) {
}
