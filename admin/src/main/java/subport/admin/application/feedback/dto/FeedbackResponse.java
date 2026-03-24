package subport.admin.application.feedback.dto;

import java.time.LocalDateTime;

import subport.domain.feedback.Feedback;

public record FeedbackResponse(
	Long id,
	String category,
	String subCategory,
	String content,
	LocalDateTime createdAt
) {

	public static FeedbackResponse from(Feedback feedback) {
		return new FeedbackResponse(
			feedback.getId(),
			feedback.getCategory().name(),
			feedback.getSubCategory(),
			feedback.getContent(),
			feedback.getCreatedAt()
		);
	}
}
