package subport.admin.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.admin.adapter.in.security.AdminUserDetails;
import subport.admin.application.account.AdminAccountCommandService;
import subport.admin.application.account.AdminAccountQueryService;
import subport.admin.application.account.dto.GetAdminProfileResponse;
import subport.admin.application.account.dto.UpdateAdminPasswordRequest;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AccountController {

	private final AdminAccountCommandService adminAccountCommandService;
	private final AdminAccountQueryService adminAccountQueryService;

	@PutMapping("/password")
	public ResponseEntity<Void> updatePassword(
		@AuthenticationPrincipal AdminUserDetails userDetails,
		@Valid @RequestBody UpdateAdminPasswordRequest request
	) {
		adminAccountCommandService.updatePassword(userDetails.getAdminId(), request);

		return ResponseEntity.noContent()
			.build();
	}

	@GetMapping("/profile")
	public ResponseEntity<GetAdminProfileResponse> getProfile(@AuthenticationPrincipal AdminUserDetails userDetails) {
		return ResponseEntity.ok(
			adminAccountQueryService.getProfile(userDetails.getAdminId())
		);
	}
}
