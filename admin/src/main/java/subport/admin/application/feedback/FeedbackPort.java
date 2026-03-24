package subport.admin.application.feedback;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import subport.domain.feedback.Feedback;
import subport.domain.feedback.FeedbackCategory;

public interface FeedbackPort {

	Page<Feedback> loadFeedbacks(
		LocalDateTime start,
		LocalDateTime end,
		FeedbackCategory category,
		Pageable pageable
	);
}
