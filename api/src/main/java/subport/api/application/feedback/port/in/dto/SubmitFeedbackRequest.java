package subport.api.application.feedback.port.in.dto;

public record SubmitFeedbackRequest(
	String category,
	String subCategory,
	String content
) {
}
