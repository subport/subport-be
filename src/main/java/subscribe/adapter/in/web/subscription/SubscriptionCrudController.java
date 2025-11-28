package subscribe.adapter.in.web.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subscribe.application.subscribe.port.in.SaveSubscriptionRequest;
import subscribe.application.subscribe.port.in.SaveSubscriptionUseCase;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionCrudController {

	private final SaveSubscriptionUseCase saveSubscriptionUseCase;

	public ResponseEntity<Void> saveSubscription(
		@AuthenticationPrincipal OAuth2User oAuth2User,
		@RequestBody SaveSubscriptionRequest request
	) {
		Long memberId = Long.valueOf(oAuth2User.getName());
		saveSubscriptionUseCase.saveSubscription(
			memberId,
			request
		);

		return ResponseEntity
			.ok()
			.build();
	}
}
