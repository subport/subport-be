package subport.adapter.in.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminUpdatePlanRequest;
import subport.application.admin.service.AdminPlanService;

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
}
