package subport.admin.application.dto;

import java.util.List;

public record AdminMembersResponse(
	List<AdminMemberResponse> members,
	int currentPage,
	long totalElements,
	int totalPages
) {
}
