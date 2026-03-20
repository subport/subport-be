package subport.api.adapter.in.web.feedback;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;
import subport.api.application.feedback.port.in.SubmitTestFeedbackUseCase;
import subport.api.application.feedback.port.in.dto.SubmitTestFeedbackRequest;

@RestController
@RequestMapping("/api/test-feedbacks")
@RequiredArgsConstructor
public class TestFeedbackController {

	private final SubmitTestFeedbackUseCase submitTestFeedbackUseCase;

	@PostMapping
	public ResponseEntity<Void> testSubmitFeedback(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody SubmitTestFeedbackRequest request
	) {
		submitTestFeedbackUseCase.submitTestFeedback(oAuth2User.getMemberId(), request);

		return ResponseEntity.ok()
			.build();
	}
}
