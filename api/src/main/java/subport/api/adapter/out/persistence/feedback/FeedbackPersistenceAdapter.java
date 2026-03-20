package subport.api.adapter.out.persistence.feedback;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.feedback.port.out.SaveFeedbackPort;
import subport.domain.feedback.Feedback;

@Component
@RequiredArgsConstructor
public class FeedbackPersistenceAdapter implements SaveFeedbackPort {

	private final SpringDataFeedbackRepository feedbackRepository;

	@Override
	public void save(Feedback feedback) {
		feedbackRepository.save(feedback);
	}
}
