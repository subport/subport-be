package subport.api.adapter.in.web.feedback;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;
import subport.api.application.feedback.port.in.SubmitFeedbackUseCase;
import subport.api.application.feedback.port.in.dto.SubmitFeedbackRequest;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

	private final SubmitFeedbackUseCase submitFeedbackUseCase;

	@PostMapping
	public ResponseEntity<Void> submitFeedback(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody SubmitFeedbackRequest request
	) {
		submitFeedbackUseCase.submitFeedback(oAuth2User.getMemberId(), request);

		return ResponseEntity.ok()
			.build();
	}
}
