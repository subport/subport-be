package subport.adapter.in.web.membersubscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.membersubscription.port.in.DeleteMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.ReadMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionDutchPayUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionMemoUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionPlanUseCase;
import subport.application.membersubscription.port.in.UpdateMemberSubscriptionReminderUseCase;
import subport.application.membersubscription.port.in.dto.ReadMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.dto.RegisterMemberSubscriptionResponse;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionDutchPayRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionMemoRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionPlanRequest;
import subport.application.membersubscription.port.in.dto.UpdateMemberSubscriptionReminderRequest;

@RestController
@RequestMapping("/api/member-subscriptions")
@RequiredArgsConstructor
public class MemberSubscriptionController {

	private final RegisterMemberSubscriptionUseCase registerMemberSubscriptionUseCase;
	private final UpdateMemberSubscriptionPlanUseCase updateMemberSubscriptionPlanUseCase;
	private final UpdateMemberSubscriptionDutchPayUseCase updateMemberSubscriptionDutchPayUseCase;
	private final UpdateMemberSubscriptionReminderUseCase updateMemberSubscriptionReminderUseCase;
	private final UpdateMemberSubscriptionMemoUseCase updateMemberSubscriptionMemoUseCase;
	private final DeleteMemberSubscriptionUseCase deleteMemberSubscriptionUseCase;
	private final ReadMemberSubscriptionUseCase readMemberSubscriptionUseCase;

	@PostMapping
	public ResponseEntity<RegisterMemberSubscriptionResponse> registerMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody RegisterMemberSubscriptionRequest request
	) {
		return ResponseEntity.ok(registerMemberSubscriptionUseCase.register(
			oAuth2User.getMemberId(),
			request));
	}

	@PutMapping("/{id}/plan")
	public ResponseEntity<Void> updateMemberSubscriptionPlan(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateMemberSubscriptionPlanRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		updateMemberSubscriptionPlanUseCase.updatePlan(
			oAuth2User.getMemberId(),
			request,
			memberSubscriptionId
		);

		return ResponseEntity.noContent()
			.build();
	}

	@PutMapping("/{id}/dutch-pay")
	public ResponseEntity<Void> updateMemberSubscriptionDutchPay(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateMemberSubscriptionDutchPayRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		updateMemberSubscriptionDutchPayUseCase.updateDutchPay(
			oAuth2User.getMemberId(),
			request,
			memberSubscriptionId
		);

		return ResponseEntity.noContent()
			.build();
	}

	@PutMapping("/{id}/reminder")
	public ResponseEntity<Void> updateMemberSubscriptionReminder(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateMemberSubscriptionReminderRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		updateMemberSubscriptionReminderUseCase.updateReminder(
			oAuth2User.getMemberId(),
			request,
			memberSubscriptionId
		);

		return ResponseEntity.noContent()
			.build();
	}

	@PutMapping("/{id}/memo")
	public ResponseEntity<Void> updateMemberSubscriptionMemo(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateMemberSubscriptionMemoRequest request,
		@PathVariable("id") Long memberSubscriptionId
	) {
		updateMemberSubscriptionMemoUseCase.updateMemo(
			oAuth2User.getMemberId(),
			request,
			memberSubscriptionId
		);

		return ResponseEntity.noContent()
			.build();
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

	@GetMapping("/{id}")
	public ResponseEntity<ReadMemberSubscriptionResponse> readMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@PathVariable("id") Long memberSubscriptionId
	) {
		return ResponseEntity.ok(readMemberSubscriptionUseCase.read(
			oAuth2User.getMemberId(), memberSubscriptionId));
	}
}
