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
import subport.admin.application.feedback.TestFeedbackQueryService;
import subport.admin.application.feedback.dto.TestFeedbacksResponse;

@RestController
@RequestMapping("/admin/test-feedbacks")
@RequiredArgsConstructor
public class TestFeedbackController {

	private final TestFeedbackQueryService testFeedbackQueryService;

	@GetMapping
	public ResponseEntity<TestFeedbacksResponse> getTestFeedbacks(
		@RequestParam(required = false) LocalDate date,
		@PageableDefault(
			sort = "createdAt",
			direction = Sort.Direction.DESC,
			size = 15
		) Pageable pageable
	) {
		return ResponseEntity.ok(
			testFeedbackQueryService.getTestFeedbacks(date, pageable)
		);
	}
}
