package subport.adapter.in.web.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscribe.port.in.RegisterCustomSubscriptionUseCase;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final RegisterCustomSubscriptionUseCase registerCustomSubscriptionUseCase;

	@PostMapping
	public ResponseEntity<Void> saveCustomSubscription(
		@AuthenticationPrincipal OAuth2User oAuth2User,
		@RequestPart RegisterCustomSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		Long memberId = Long.valueOf(oAuth2User.getName());
		registerCustomSubscriptionUseCase.register(
			memberId,
			request,
			image
		);

		return ResponseEntity
			.ok()
			.build();
	}
}
