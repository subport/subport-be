package subport.admin.adapter.out.persistence.feedback;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.feedback.TestFeedbackPort;
import subport.domain.feedback.TestFeedback;

@Component
@RequiredArgsConstructor
public class TestFeedbackAdapter implements TestFeedbackPort {

	private final SpringDataTestFeedbackRepository feedbackRepository;

	@Override
	public Page<TestFeedback> loadTestFeedbacks(
		LocalDateTime start,
		LocalDateTime end,
		Pageable pageable
	) {
		return feedbackRepository.findTestFeedbacks(start, end, pageable);
	}
}
