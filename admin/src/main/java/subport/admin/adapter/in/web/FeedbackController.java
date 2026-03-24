package subport.admin.adapter.in.web;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.feedback.FeedbackQueryService;
import subport.admin.application.feedback.dto.FeedbacksResponse;
import subport.domain.feedback.FeedbackCategory;

@RestController
@RequestMapping("/admin/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

	private final FeedbackQueryService feedbackQueryService;

	@GetMapping
	public ResponseEntity<FeedbacksResponse> getFeedbacks(
		@RequestParam(required = false) LocalDate date,
		@RequestParam(required = false) FeedbackCategory category,
		@PageableDefault(
			sort = "createdAt",
			direction = Sort.Direction.DESC,
			size = 15
		) Pageable pageable
	) {
		return ResponseEntity.ok(
			feedbackQueryService.getFeedbacks(date, category, pageable)
		);
	}
}
