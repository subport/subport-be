package subport.admin.adapter.in.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import lombok.RequiredArgsConstructor;
import subport.admin.application.plan.PlanCommandService;
import subport.admin.application.plan.PlanQueryService;
import subport.admin.application.plan.dto.PlansResponse;
import subport.admin.application.plan.dto.RegisterPlanRequest;
import subport.admin.application.subscription.SubscriptionCommandService;
import subport.admin.application.subscription.SubscriptionQueryService;
import subport.admin.application.subscription.dto.RegisterSubscriptionRequest;
import subport.admin.application.subscription.dto.SubscriptionResponse;
import subport.admin.application.subscription.dto.SubscriptionsResponse;
import subport.admin.application.subscription.dto.UpdateSubscriptionRequest;

@RestController
@RequestMapping("/admin/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

	private final SubscriptionCommandService subscriptionCommandService;
	private final SubscriptionQueryService subscriptionQueryService;
	private final PlanCommandService planCommandService;
	private final PlanQueryService planQueryService;

	@PostMapping
	public ResponseEntity<Long> registerSubscription(
		@RequestPart RegisterSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		return ResponseEntity.ok(
			subscriptionCommandService.registerSubscription(request, image)
		);
	}

	@GetMapping
	public ResponseEntity<SubscriptionsResponse> searchSubscriptions(
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String name,
		@PageableDefault(
			sort = "id",
			direction = Sort.Direction.ASC
		) Pageable pageable
	) {
		return ResponseEntity.ok(
			subscriptionQueryService.searchSubscriptions(type, name, pageable)
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable("id") Long subscriptionId) {
		return ResponseEntity.ok(subscriptionQueryService.getSubscription(subscriptionId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateSubscription(
		@PathVariable("id") Long subscriptionId,
		@RequestPart UpdateSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		subscriptionCommandService.updateSubscription(subscriptionId, request, image);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubscription(@PathVariable("id") Long subscriptionId) {
		subscriptionCommandService.deleteSubscription(subscriptionId);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/plans")
	public ResponseEntity<Void> registerPlan(
		@PathVariable("id") Long subscriptionId,
		@RequestBody RegisterPlanRequest request
	) {
		planCommandService.registerPlan(subscriptionId, request);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/plans")
	public ResponseEntity<PlansResponse> getPlans(@PathVariable("id") Long subscriptionId) {
		return ResponseEntity.ok(planQueryService.getPlans(subscriptionId));
	}
}
