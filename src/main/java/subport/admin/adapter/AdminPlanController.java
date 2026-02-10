package subport.admin.adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminUpdatePlanRequest;
import subport.admin.application.service.AdminPlanService;

@RestController
@RequestMapping("/admin/plans")
@RequiredArgsConstructor
public class AdminPlanController {

	private final AdminPlanService adminPlanService;

	@PutMapping("/{id}")
	public ResponseEntity<Void> updatePlan(
		@PathVariable("id") Long planId,
		@RequestBody AdminUpdatePlanRequest request
	) {
		adminPlanService.updatePlan(planId, request);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlan(@PathVariable("id") Long planId) {
		adminPlanService.deletePlan(planId);

		return ResponseEntity.noContent().build();
	}
}
