package subport.admin.adapter;

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
import subport.admin.application.dto.AdminPlansResponse;
import subport.admin.application.dto.AdminRegisterPlanRequest;
import subport.admin.application.dto.AdminRegisterSubscriptionRequest;
import subport.admin.application.dto.AdminSubscriptionResponse;
import subport.admin.application.dto.AdminSubscriptionsResponse;
import subport.admin.application.dto.AdminUpdateSubscriptionRequest;
import subport.admin.application.service.AdminPlanService;
import subport.admin.application.service.AdminSubscriptionService;

@RestController
@RequestMapping("/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

	private final AdminSubscriptionService subscriptionService;
	private final AdminPlanService planService;

	@PostMapping
	public ResponseEntity<Long> registerSubscription(
		@RequestPart AdminRegisterSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		return ResponseEntity.ok(
			subscriptionService.registerSubscription(request, image)
		);
	}

	@GetMapping
	public ResponseEntity<AdminSubscriptionsResponse> searchSubscriptions(
		@RequestParam(required = false) String type,
		@RequestParam(required = false) String name,
		@PageableDefault(
			page = 0,
			size = 10,
			sort = "id",
			direction = Sort.Direction.ASC
		) Pageable pageable
	) {
		return ResponseEntity.ok(
			subscriptionService.searchSubscriptions(type, name, pageable)
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminSubscriptionResponse> getSubscription(@PathVariable("id") Long subscriptionId) {
		return ResponseEntity.ok(subscriptionService.getSubscription(subscriptionId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateSubscription(
		@PathVariable("id") Long subscriptionId,
		@RequestPart AdminUpdateSubscriptionRequest request,
		@RequestPart(required = false) MultipartFile image
	) {
		subscriptionService.updateSubscription(subscriptionId, request, image);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubscription(@PathVariable("id") Long subscriptionId) {
		subscriptionService.deleteSubscription(subscriptionId);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/plans")
	public ResponseEntity<Void> registerPlan(
		@PathVariable("id") Long subscriptionId,
		@RequestBody AdminRegisterPlanRequest request
	) {
		planService.registerPlan(subscriptionId, request);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/plans")
	public ResponseEntity<AdminPlansResponse> getPlans(@PathVariable("id") Long subscriptionId) {
		return ResponseEntity.ok(planService.getPlans(subscriptionId));
	}
}
