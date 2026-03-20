package subport.api.application.feedback.port.in;

import subport.api.application.feedback.port.in.dto.SubmitTestFeedbackRequest;

public interface SubmitTestFeedbackUseCase {

	void submitTestFeedback(Long memberId, SubmitTestFeedbackRequest request);
}
