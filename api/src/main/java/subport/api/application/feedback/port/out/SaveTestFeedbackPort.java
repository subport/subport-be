package subport.api.application.feedback.port.out;

import subport.domain.feedback.TestFeedback;

public interface SaveTestFeedbackPort {

	void save(TestFeedback testFeedback);
}
