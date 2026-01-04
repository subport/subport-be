package subport.adapter.in.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import subport.application.member.port.in.UpdateMemberRequest;
import subport.application.member.port.in.UpdateMemberUseCase;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberCrudController {

	private final UpdateMemberUseCase updateMemberUseCase;

	@PutMapping
	public ResponseEntity<Void> updateMember(
		@AuthenticationPrincipal OAuth2User oAuth2User,
		@RequestBody UpdateMemberRequest request
	) {
		Long memberId = Long.valueOf(oAuth2User.getName());
		updateMemberUseCase.updateMember(
			memberId,
			request
		);

		return ResponseEntity
			.noContent()
			.build();
	}
}
