package subport.api.adapter.in.web.member;

import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.api.adapter.in.security.oauth2.CustomOAuth2User;
import subport.api.adapter.in.web.AuthCookieProvider;
import subport.api.application.member.port.in.DeleteMemberUseCase;
import subport.api.application.member.port.in.MemberQueryUseCase;
import subport.api.application.member.port.in.UpdateMemberUseCase;
import subport.api.application.member.port.in.dto.GetMemberProfileResponse;
import subport.api.application.member.port.in.dto.GetMemberResponse;
import subport.api.application.member.port.in.dto.GetReminderSettingsResponse;
import subport.api.application.member.port.in.dto.UpdateMemberRequest;
import subport.api.application.member.port.in.dto.UpdateReminderSettingsRequest;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberQueryUseCase memberQueryUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;
	private final DeleteMemberUseCase deleteMemberUseCase;

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
	public ResponseEntity<GetReminderSettingsResponse> getReminderSettings(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		return ResponseEntity.ok(
			memberQueryUseCase.getMemberReminderSettings(oAuth2User.getMemberId())
		);
	}

	@PutMapping("/me/reminder-settings")
	public ResponseEntity<GetReminderSettingsResponse> updateReminderSettings(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateReminderSettingsRequest request
	) {
		return ResponseEntity.ok(
			updateMemberUseCase.updateReminderSettings(oAuth2User.getMemberId(), request)
		);
	}

	@DeleteMapping("/me")
	public ResponseEntity<Void> deleteMember(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User
	) {
		deleteMemberUseCase.deleteMember(oAuth2User.getMemberId());

		return ResponseEntity.noContent()
			.header(
				HttpHeaders.SET_COOKIE,
				AuthCookieProvider.deleteRefreshTokenCookie().toString()
			)
			.build();
	}
}
