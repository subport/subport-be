package subport.admin.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.plan.PlanCommandService;
import subport.admin.application.plan.dto.UpdatePlanRequest;

@RestController
@RequestMapping("/admin/plans")
@RequiredArgsConstructor
public class PlanController {

	private final PlanCommandService planCommandService;

	@PutMapping("/{id}")
	public ResponseEntity<Void> updatePlan(
		@PathVariable("id") Long planId,
		@RequestBody UpdatePlanRequest request
	) {
		planCommandService.updatePlan(planId, request);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlan(@PathVariable("id") Long planId) {
		planCommandService.deletePlan(planId);

		return ResponseEntity.noContent().build();
	}
}
