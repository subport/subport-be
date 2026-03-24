package subport.admin.adapter.out.persistence.feedback;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.feedback.FeedbackPort;
import subport.domain.feedback.Feedback;
import subport.domain.feedback.FeedbackCategory;

@Component
@RequiredArgsConstructor
public class FeedbackAdapter implements FeedbackPort {

	private final SpringDataFeedbackRepository feedbackRepository;

	@Override
	public Page<Feedback> loadFeedbacks(
		LocalDateTime start,
		LocalDateTime end,
		FeedbackCategory category,
		Pageable pageable
	) {
		return feedbackRepository.findFeedbacks(start, end, category, pageable);
	}
}
