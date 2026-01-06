package subport.adapter.in.web.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.subscription.port.in.RegisterCustomSubscriptionPlanRequest;
import subport.application.subscription.port.in.RegisterCustomSubscriptionPlanUseCase;
import subport.application.subscription.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscription.port.in.RegisterCustomSubscriptionUseCase;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final RegisterCustomSubscriptionUseCase registerCustomSubscriptionUseCase;
	private final RegisterCustomSubscriptionPlanUseCase registerCustomSubscriptionPlanUseCase;

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

	@PostMapping("/{id}/plans")
	public ResponseEntity<Void> registerCustomSubscriptionPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody RegisterCustomSubscriptionPlanRequest request,
		@PathVariable("id") Long subscriptionId
	) {
		registerCustomSubscriptionPlanUseCase.register(
			oAuth2User.getMemberId(),
			request,
			subscriptionId
		);

		return ResponseEntity.ok()
			.build();
	}
}
