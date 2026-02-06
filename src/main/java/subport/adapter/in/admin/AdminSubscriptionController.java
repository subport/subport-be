package subport.adapter.in.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminPlansResponse;
import subport.application.admin.dto.AdminSubscriptionResponse;
import subport.application.admin.dto.AdminSubscriptionsResponse;
import subport.application.admin.service.AdminPlanService;
import subport.application.admin.service.AdminSubscriptionService;

@RestController
@RequestMapping("/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

	private final AdminSubscriptionService subscriptionService;
	private final AdminPlanService planService;

	@GetMapping
	public ResponseEntity<AdminSubscriptionsResponse> searchSubscriptions() {
		return ResponseEntity.ok(subscriptionService.searchSubscriptions());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminSubscriptionResponse> getSubscription(@PathVariable("id") Long subscriptionId) {
		return ResponseEntity.ok(subscriptionService.getSubscription(subscriptionId));
	}

	@GetMapping("/{id}/plans")
	public ResponseEntity<AdminPlansResponse> getPlans(@PathVariable("id") Long subscriptionId) {
		return ResponseEntity.ok(planService.getPlans(subscriptionId));
	}
}
