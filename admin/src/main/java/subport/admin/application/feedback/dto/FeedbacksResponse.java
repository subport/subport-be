package subport.admin.application.feedback.dto;

import java.util.List;

public record FeedbacksResponse(
	List<FeedbackResponse> feedbacks,
	int currentPage,
	long totalElements,
	int totalPages
) {
}
