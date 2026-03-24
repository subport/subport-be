package subport.admin.application.feedback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.feedback.dto.FeedbackResponse;
import subport.admin.application.feedback.dto.FeedbacksResponse;
import subport.domain.feedback.Feedback;
import subport.domain.feedback.FeedbackCategory;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackQueryService {

	private final FeedbackPort feedbackPort;

	public FeedbacksResponse getFeedbacks(
		LocalDate date,
		String category,
		Pageable pageable
	) {
		LocalDateTime start = null;
		LocalDateTime end = null;
		if (date != null) {
			start = date.atStartOfDay();
			end = start.plusDays(1);
		}

		Page<Feedback> feedbacksPage = feedbackPort.loadFeedbacks(
			start,
			end,
			FeedbackCategory.from(category),
			pageable
		);

		List<FeedbackResponse> feedbacks = feedbacksPage.getContent().stream()
			.map(FeedbackResponse::from)
			.toList();

		return new FeedbacksResponse(
			feedbacks,
			feedbacksPage.getNumber(),
			feedbacksPage.getTotalElements(),
			feedbacksPage.getTotalPages()
		);
	}
}
