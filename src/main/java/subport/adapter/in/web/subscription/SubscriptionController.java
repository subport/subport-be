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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.subscription.port.in.DeleteCustomSubscriptionUseCase;
import subport.application.subscription.port.in.GetSubscriptionTypesUseCase;
import subport.application.subscription.port.in.PlanQueryUseCase;
import subport.application.subscription.port.in.RegisterCustomPlanUseCase;
import subport.application.subscription.port.in.RegisterCustomSubscriptionUseCase;
import subport.application.subscription.port.in.SubscriptionQueryUseCase;
import subport.application.subscription.port.in.UpdateCustomSubscriptionUseCase;
import subport.application.subscription.port.in.dto.GetPlansResponse;
import subport.application.subscription.port.in.dto.GetSubscriptionResponse;
import subport.application.subscription.port.in.dto.GetSubscriptionsResponse;
import subport.application.subscription.port.in.dto.RegisterCustomPlanRequest;
import subport.application.subscription.port.in.dto.RegisterCustomPlanResponse;
import subport.application.subscription.port.in.dto.RegisterCustomSubscriptionRequest;
import subport.application.subscription.port.in.dto.RegisterCustomSubscriptionResponse;
import subport.application.subscription.port.in.dto.UpdateCustomSubscriptionRequest;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final RegisterCustomSubscriptionUseCase registerCustomSubscriptionUseCase;
	private final SubscriptionQueryUseCase subscriptionQueryUseCase;
	private final UpdateCustomSubscriptionUseCase updateCustomSubscriptionUseCase;
	private final DeleteCustomSubscriptionUseCase deleteCustomSubscriptionUseCase;

	private final RegisterCustomPlanUseCase registerCustomPlanUseCase;
	private final PlanQueryUseCase planQueryUseCase;

	private final GetSubscriptionTypesUseCase getSubscriptionTypesUseCase;

	@PostMapping
	public ResponseEntity<RegisterCustomSubscriptionResponse> registerCustomSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestPart RegisterCustomSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		return ResponseEntity.ok(registerCustomSubscriptionUseCase.register(
			oAuth2User.getMemberId(),
			request,
			image));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetSubscriptionResponse> getSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId
	) {
		return ResponseEntity.ok(subscriptionQueryUseCase.getSubscription(
			oAuth2User.getMemberId(),
			subscriptionId));
	}

	@GetMapping
	public ResponseEntity<GetSubscriptionsResponse> searchSubscriptions(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestParam(required = false) String name
	) {
		return ResponseEntity.ok(subscriptionQueryUseCase.searchSubscriptions(
			oAuth2User.getMemberId(), name));
	}

	@PutMapping("/{id}")
	public ResponseEntity<GetSubscriptionResponse> updateCustomSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId,
		@Valid @RequestPart UpdateCustomSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		return ResponseEntity.ok(
			updateCustomSubscriptionUseCase.update(
				oAuth2User.getMemberId(),
				subscriptionId,
				request,
				image
			)
		);
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

	@PostMapping("/{id}/plans")
	public ResponseEntity<RegisterCustomPlanResponse> registerCustomPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody RegisterCustomPlanRequest request,
		@PathVariable("id") Long subscriptionId
	) {
		return ResponseEntity.ok(registerCustomPlanUseCase.register(
			oAuth2User.getMemberId(),
			request,
			subscriptionId));
	}

	@GetMapping("/{id}/plans")
	public ResponseEntity<GetPlansResponse> getPlans(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long subscriptionId
	) {
		return ResponseEntity.ok(planQueryUseCase.getPlans(
			oAuth2User.getMemberId(),
			subscriptionId));
	}

	@GetMapping("/types")
	public ResponseEntity<List<String>> getSubscriptionTypes() {
		return ResponseEntity.ok(getSubscriptionTypesUseCase.get());
	}
}
