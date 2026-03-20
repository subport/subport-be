package subport.api.application.feedback.port.out;

import subport.domain.feedback.Feedback;

public interface SaveFeedbackPort {

	void save(Feedback feedback);
}
