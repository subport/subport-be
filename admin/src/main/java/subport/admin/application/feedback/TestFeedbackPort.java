package subport.admin.application.feedback;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import subport.domain.feedback.TestFeedback;

public interface TestFeedbackPort {

	Page<TestFeedback> loadTestFeedbacks(
		LocalDateTime start,
		LocalDateTime end,
		Pageable pageable
	);
}
