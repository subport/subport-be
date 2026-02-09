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
import subport.application.member.port.in.MemberQueryUseCase;
import subport.application.member.port.in.UpdateMemberUseCase;
import subport.application.member.port.in.dto.GetMemberProfileResponse;
import subport.application.member.port.in.dto.GetMemberReminderSettingsResponse;
import subport.application.member.port.in.dto.GetMemberResponse;
import subport.application.member.port.in.dto.UpdateMemberRequest;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberQueryUseCase memberQueryUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;

	@GetMapping("/me/profile")
	public ResponseEntity<GetMemberProfileResponse> getMemberProfile(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		return ResponseEntity.ok(memberQueryUseCase.getMemberProfile(
			oAuth2User.getMemberId(), LocalDate.now()));
	}

	@GetMapping("/me")
	public ResponseEntity<GetMemberResponse> getMember(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
		return ResponseEntity.ok(
			memberQueryUseCase.getMember(oAuth2User.getMemberId())
		);
	}

	@PutMapping("/me")
	public ResponseEntity<GetMemberResponse> updateMember(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@Valid @RequestBody UpdateMemberRequest request
	) {
		return ResponseEntity.ok(
			updateMemberUseCase.updateMember(oAuth2User.getMemberId(), request)
		);
	}

	@GetMapping("/me/reminder-settings")
	public ResponseEntity<GetMemberReminderSettingsResponse> getReminderSettings(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		return ResponseEntity.ok(
			memberQueryUseCase.getMemberReminderSettings(oAuth2User.getMemberId())
		);
	}
}
