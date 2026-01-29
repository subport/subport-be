package subport.adapter.in.web.member;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.member.port.in.GetMemberProfileUseCase;
import subport.application.member.port.in.GetMemberUseCase;
import subport.application.member.port.in.UpdateMemberUseCase;
import subport.application.member.port.in.dto.GetMemberProfileResponse;
import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.UpdateMemberRequest;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final GetMemberProfileUseCase getMemberProfileUseCase;
	private final GetMemberUseCase getMemberUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;

	@GetMapping("/me/profile")
	public ResponseEntity<GetMemberProfileResponse> getMemberProfile(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
		return ResponseEntity.ok(getMemberProfileUseCase.get(
			oAuth2User.getMemberId(), LocalDate.now()));
	}

	@GetMapping("/me")
	public ResponseEntity<GetMemberResponse> getMember(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
		return ResponseEntity.ok(
			getMemberUseCase.get(oAuth2User.getMemberId())
		);
	}

	@PutMapping("/me")
	public ResponseEntity<Void> updateMember(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody UpdateMemberRequest request
	) {
		updateMemberUseCase.update(oAuth2User.getMemberId(), request);

		return ResponseEntity.noContent()
			.build();
	}
}
