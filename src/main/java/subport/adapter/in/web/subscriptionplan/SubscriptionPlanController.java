package subport.adapter.in.web.subscriptionplan;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.subscription.port.in.DeleteCustomPlanUseCase;
import subport.application.subscription.port.in.UpdateCustomSubscriptionPlanRequest;
import subport.application.subscription.port.in.UpdateCustomSubscriptionPlanUseCase;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class SubscriptionPlanController {

	private final UpdateCustomSubscriptionPlanUseCase updateCustomSubscriptionPlanUseCase;
	private final DeleteCustomPlanUseCase deleteCustomPlanUseCase;

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateSubscriptionPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateCustomSubscriptionPlanRequest request,
		@PathVariable("id") Long subscriptionPlanId
	) {
		updateCustomSubscriptionPlanUseCase.update(
			oAuth2User.getMemberId(),
			request,
			subscriptionPlanId
		);

		return ResponseEntity.noContent()
			.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubscriptionPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long planId
	) {
		deleteCustomPlanUseCase.delete(oAuth2User.getMemberId(), planId);

		return ResponseEntity.noContent()
			.build();
	}
}
