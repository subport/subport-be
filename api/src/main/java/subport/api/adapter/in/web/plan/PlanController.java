package subport.api.adapter.in.web.plan;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;
import subport.api.application.plan.port.in.DeleteCustomPlanUseCase;
import subport.api.application.plan.port.in.PlanQueryUseCase;
import subport.api.application.plan.port.in.UpdateCustomPlanUseCase;
import subport.api.application.plan.port.in.dto.GetPlanResponse;
import subport.api.application.plan.port.in.dto.UpdateCustomPlanRequest;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

	private final PlanQueryUseCase planQueryUseCase;
	private final UpdateCustomPlanUseCase updateCustomPlanUseCase;
	private final DeleteCustomPlanUseCase deleteCustomPlanUseCase;

	@GetMapping("/{id}")
	public ResponseEntity<GetPlanResponse> getPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long planId
	) {
		return ResponseEntity.ok(planQueryUseCase.getPlan(
			oAuth2User.getMemberId(), planId));
	}

	@PutMapping("/{id}")
	public ResponseEntity<GetPlanResponse> updateCustomPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody UpdateCustomPlanRequest request,
		@PathVariable("id") Long planId
	) {
		return ResponseEntity.ok(
			updateCustomPlanUseCase.update(
				oAuth2User.getMemberId(),
				request,
				planId
			)
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long planId
	) {
		deleteCustomPlanUseCase.delete(oAuth2User.getMemberId(), planId);

		return ResponseEntity.noContent()
			.build();
	}
}
