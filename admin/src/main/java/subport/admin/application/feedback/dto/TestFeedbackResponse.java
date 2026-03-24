package subport.admin.application.feedback.dto;

import java.time.LocalDateTime;

import subport.domain.feedback.TestFeedback;

public record TestFeedbackResponse(
	Long id,
	String overall,
	String featureRequest,
	LocalDateTime createdAt
) {

	public static TestFeedbackResponse from(TestFeedback testFeedback) {
		return new TestFeedbackResponse(
			testFeedback.getId(),
			testFeedback.getOverall(),
			testFeedback.getFeatureRequest(),
			testFeedback.getCreatedAt()
		);
	}
}
