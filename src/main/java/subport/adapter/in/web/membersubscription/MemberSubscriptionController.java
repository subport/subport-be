package subport.adapter.in.web.membersubscription;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.membersubscription.port.in.ActivateMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.DeactivateMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.DeleteMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.MemberSubscriptionQueryUseCase;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionDutchPayUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionMemoUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanUseCase;
import subport.application.membersubscription.port.in.dto.ActivateMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.GetMemberSubscriptionsResponse;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionDutchPayRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionMemoRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionPlanRequest;

@RestController
@RequestMapping("/api/member-subscriptions")
@RequiredArgsConstructor
public class MemberSubscriptionController {

	private final RegisterMemberSubscriptionUseCase registerMemberSubscriptionUseCase;
	private final UpdateMemberSubscriptionPlanUseCase updateMemberSubscriptionPlanUseCase;
	private final UpdateMemberSubscriptionDutchPayUseCase updateMemberSubscriptionDutchPayUseCase;
	private final UpdateMemberSubscriptionMemoUseCase updateMemberSubscriptionMemoUseCase;
	private final DeactivateMemberSubscriptionUseCase deactivateMemberSubscriptionUseCase;
	private final ActivateMemberSubscriptionUseCase activateMemberSubscriptionUseCase;
	private final DeleteMemberSubscriptionUseCase deleteMemberSubscriptionUseCase;
	private final MemberSubscriptionQueryUseCase memberSubscriptionQueryUseCase;

	@PostMapping
	public ResponseEntity<RegisterMemberSubscriptionResponse> registerMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody RegisterMemberSubscriptionRequest request
	) {
		return ResponseEntity.ok(registerMemberSubscriptionUseCase.register(
			oAuth2User.getMemberId(),
			request,
			LocalDateTime.now()
		));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetMemberSubscriptionResponse> getMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(memberSubscriptionQueryUseCase.getMemberSubscription(
			oAuth2User.getMemberId(),
			memberSubscriptionId,
			LocalDate.now()
		));
	}

	@GetMapping
	public ResponseEntity<GetMemberSubscriptionsResponse> getMemberSubscriptions(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestParam(defaultValue = "true") boolean active,
		@RequestParam(defaultValue = "type") String sortBy
	) {
		return ResponseEntity.ok(memberSubscriptionQueryUseCase.getMemberSubscriptions(
			oAuth2User.getMemberId(),
			active,
			sortBy,
			LocalDate.now()
		));
	}

	@PutMapping("/{id}/plan")
	public ResponseEntity<GetMemberSubscriptionResponse> updateMemberSubscriptionPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody UpdateMemberSubscriptionPlanRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(
			updateMemberSubscriptionPlanUseCase.updatePlan(
				oAuth2User.getMemberId(),
				request,
				memberSubscriptionId,
				LocalDateTime.now()
			)
		);
	}

	@PutMapping("/{id}/dutch-pay")
	public ResponseEntity<GetMemberSubscriptionResponse> updateMemberSubscriptionDutchPay(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateMemberSubscriptionDutchPayRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(
			updateMemberSubscriptionDutchPayUseCase.updateDutchPay(
				oAuth2User.getMemberId(),
				request,
				memberSubscriptionId,
				LocalDate.now()
			)
		);
	}

	@PutMapping("/{id}/memo")
	public ResponseEntity<GetMemberSubscriptionResponse> updateMemberSubscriptionMemo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody UpdateMemberSubscriptionMemoRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(
			updateMemberSubscriptionMemoUseCase.updateMemo(
				oAuth2User.getMemberId(),
				request,
				memberSubscriptionId,
				LocalDate.now()
			)
		);
	}

	@PutMapping("/{id}/deactivate")
	public ResponseEntity<GetMemberSubscriptionResponse> deactivateMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(
			deactivateMemberSubscriptionUseCase.deactivate(
				oAuth2User.getMemberId(),
				memberSubscriptionId,
				LocalDate.now()
			)
		);
	}

	@PutMapping("/{id}/activate")
	public ResponseEntity<GetMemberSubscriptionResponse> activateMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody ActivateMemberSubscriptionRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(
			activateMemberSubscriptionUseCase.activate(
				oAuth2User.getMemberId(),
				request,
				memberSubscriptionId,
				LocalDateTime.now()
			)
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long memberSubscriptionId
	) {
		deleteMemberSubscriptionUseCase.delete(oAuth2User.getMemberId(), memberSubscriptionId);

		return ResponseEntity.noContent()
			.build();
	}
}
