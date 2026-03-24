package subport.admin.application.feedback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.feedback.dto.TestFeedbackResponse;
import subport.admin.application.feedback.dto.TestFeedbacksResponse;
import subport.domain.feedback.TestFeedback;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestFeedbackQueryService {

	private final TestFeedbackPort testFeedbackPort;

	public TestFeedbacksResponse getTestFeedbacks(LocalDate date, Pageable pageable) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = start.plusDays(1);

		Page<TestFeedback> testFeedbacksPage = testFeedbackPort.loadTestFeedbacks(start, end, pageable);

		List<TestFeedbackResponse> testFeedbacks = testFeedbacksPage.getContent().stream()
			.map(TestFeedbackResponse::from)
			.toList();

		return new TestFeedbacksResponse(
			testFeedbacks,
			testFeedbacksPage.getNumber(),
			testFeedbacksPage.getTotalElements(),
			testFeedbacksPage.getTotalPages()
		);
	}
}
