package subport.adapter.in.web.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionUseCase;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final RegisterCustomSubscriptionUseCase registerCustomSubscriptionUseCase;

	@PostMapping
	public ResponseEntity<Void> registerCustomSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestPart RegisterCustomSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		registerCustomSubscriptionUseCase.register(
			oAuth2User.getMemberId(),
			request,
			image
		);

		return ResponseEntity.ok()
			.build();
	}
}
