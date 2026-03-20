package subport.api.application.feedback.port.in.dto;

public record SubmitTestFeedbackRequest(
	String overall,
	String featureRequest
) {
}
