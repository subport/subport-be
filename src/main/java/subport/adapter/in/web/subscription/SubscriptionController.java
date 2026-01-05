package subport.adapter.in.web.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		@RequestBody RegisterCustomSubscriptionRequest request
	) {
		Long memberId = Long.valueOf(oAuth2User.getName());
		registerCustomSubscriptionUseCase.register(
			memberId,
			request
		);

		return ResponseEntity
			.ok()
			.build();
	}
}
