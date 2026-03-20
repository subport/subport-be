package subport.api.application.feedback.port.in;

import subport.api.application.feedback.port.in.dto.SubmitFeedbackRequest;

public interface SubmitFeedbackUseCase {

	void submitFeedback(Long memberId, SubmitFeedbackRequest request);
}
