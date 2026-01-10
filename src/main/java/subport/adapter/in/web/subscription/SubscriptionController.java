package subport.adapter.in.web.subscription;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import subport.application.subscription.port.in.DeleteCustomSubscriptionUseCase;
import subport.application.subscription.port.in.ListSubscriptionTypesUseCase;
import subport.application.subscription.port.in.ReadSubscriptionPlansUseCase;
import subport.application.subscription.port.in.ReadSubscriptionUseCase;
import subport.application.subscription.port.in.RegisterCustomSubscriptionPlanRequest;
import subport.application.subscription.port.in.RegisterCustomSubscriptionPlanUseCase;
import subport.application.subscription.port.in.RegisterCustomSubscriptionRequest;
import subport.application.subscription.port.in.RegisterCustomSubscriptionUseCase;
import subport.application.subscription.port.in.UpdateCustomSubscriptionRequest;
import subport.application.subscription.port.in.UpdateCustomSubscriptionUseCase;
import subport.application.subscription.port.out.ListSubscriptionPlansResponse;
import subport.application.subscription.port.out.ListSubscriptionsResponse;
import subport.application.subscription.port.out.ReadSubscriptionResponse;
import subport.application.subscription.port.out.RegisterCustomSubscriptionPlanResponse;
import subport.application.subscription.port.out.RegisterCustomSubscriptionResponse;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final RegisterCustomSubscriptionUseCase registerCustomSubscriptionUseCase;
	private final UpdateCustomSubscriptionUseCase updateCustomSubscriptionUseCase;
	private final DeleteCustomSubscriptionUseCase deleteCustomSubscriptionUseCase;
	private final ReadSubscriptionUseCase readSubscriptionUseCase;

	private final RegisterCustomSubscriptionPlanUseCase registerCustomSubscriptionPlanUseCase;
	private final ReadSubscriptionPlansUseCase readSubscriptionPlansUseCase;

	private final ListSubscriptionTypesUseCase listSubscriptionTypesUseCase;

	@PostMapping
	public ResponseEntity<RegisterCustomSubscriptionResponse> registerCustomSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestPart RegisterCustomSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		return ResponseEntity.ok(registerCustomSubscriptionUseCase.register(
			oAuth2User.getMemberId(),
			request,
			image));
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

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId
	) {
		deleteCustomSubscriptionUseCase.delete(
			oAuth2User.getMemberId(),
			subscriptionId
		);

		return ResponseEntity.noContent()
			.build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReadSubscriptionResponse> readSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId
	) {
		return ResponseEntity.ok(readSubscriptionUseCase.read(
			oAuth2User.getMemberId(),
			subscriptionId));
	}

	@GetMapping
	public ResponseEntity<ListSubscriptionsResponse> listSubscriptions(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		return ResponseEntity.ok(readSubscriptionUseCase.list(oAuth2User.getMemberId()));
	}

	@PostMapping("/{id}/plans")
	public ResponseEntity<RegisterCustomSubscriptionPlanResponse> registerCustomSubscriptionPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody RegisterCustomSubscriptionPlanRequest request,
		@PathVariable("id") Long subscriptionId
	) {
		return ResponseEntity.ok(registerCustomSubscriptionPlanUseCase.register(
			oAuth2User.getMemberId(),
			request,
			subscriptionId));
	}

	@GetMapping("/{id}/plans")
	public ResponseEntity<ListSubscriptionPlansResponse> listSubscriptionPlans(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId
	) {
		return ResponseEntity.ok(readSubscriptionPlansUseCase.list(
			oAuth2User.getMemberId(),
			subscriptionId));
	}

	@GetMapping("/types")
	public ResponseEntity<List<String>> listSubscriptionTypes() {
		return ResponseEntity.ok(listSubscriptionTypesUseCase.list());
	}
}
