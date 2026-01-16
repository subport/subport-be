package subport.adapter.in.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.adapter.in.security.oauth2.CustomOAuth2User;
import subport.application.member.port.in.UpdateMemberRequest;
import subport.application.member.port.in.UpdateMemberUseCase;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final UpdateMemberUseCase updateMemberUseCase;

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateMember(
		@AuthenticationPrincipal CustomOAuth2User oAuth2User,
		@RequestBody UpdateMemberRequest request,
		@PathVariable("id") Long targetMemberId
	) {
		updateMemberUseCase.update(
			oAuth2User.getMemberId(),
			request,
			targetMemberId
		);

		return ResponseEntity
			.noContent()
			.build();
	}
}
