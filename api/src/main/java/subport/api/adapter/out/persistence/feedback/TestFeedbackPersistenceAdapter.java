package subport.api.adapter.out.persistence.feedback;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.api.application.feedback.port.out.SaveTestFeedbackPort;
import subport.domain.feedback.TestFeedback;

@Component
@RequiredArgsConstructor
public class TestFeedbackPersistenceAdapter implements SaveTestFeedbackPort {

	private final SpringDataTestFeedbackRepository testFeedbackRepository;

	@Override
	public void save(TestFeedback testFeedback) {
		testFeedbackRepository.save(testFeedback);
	}
}
