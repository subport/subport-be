package subport.admin.application.feedback.dto;

import java.util.List;

public record TestFeedbacksResponse(
	List<TestFeedbackResponse> testFeedbacks,
	int currentPage,
	long totalElements,
	int totalPages
) {
}
