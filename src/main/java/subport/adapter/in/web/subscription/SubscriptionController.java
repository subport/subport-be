package subport.adapter.in.web.subscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import subport.application.subscription.port.in.UpdateCustomSubscriptionRequest;
import subport.application.subscription.port.in.UpdateCustomSubscriptionUseCase;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final RegisterCustomSubscriptionUseCase registerCustomSubscriptionUseCase;
	private final RegisterCustomSubscriptionPlanUseCase registerCustomSubscriptionPlanUseCase;
	private final UpdateCustomSubscriptionUseCase updateCustomSubscriptionUseCase;

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

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateCustomSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId,
		@RequestPart UpdateCustomSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		updateCustomSubscriptionUseCase.update(
			oAuth2User.getMemberId(),
			subscriptionId,
			request,
			image
		);

		return ResponseEntity.noContent()
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
