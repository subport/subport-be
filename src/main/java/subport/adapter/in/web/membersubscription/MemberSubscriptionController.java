package subport.adapter.in.web.membersubscription;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionRequest;
import subport.application.membersubscription.port.in.RegisterMemberSubscriptionUseCase;
import subport.application.membersubscription.port.out.RegisterMemberSubscriptionResponse;

@RestController
@RequestMapping("/api/member-subscriptions")
@RequiredArgsConstructor
public class MemberSubscriptionController {

	private final RegisterMemberSubscriptionUseCase registerMemberSubscriptionUseCase;

	@PostMapping
	ResponseEntity<RegisterMemberSubscriptionResponse> registerMemberSubscription(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody RegisterMemberSubscriptionRequest request
	) {
		return ResponseEntity.ok(registerMemberSubscriptionUseCase.register(
			oAuth2User.getMemberId(),
			request));
	}
}
