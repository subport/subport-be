package subport.admin.adapter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import subport.admin.adapter.security.AdminUserDetails;
import subport.admin.application.dto.AdminProfileResponse;
import subport.admin.application.dto.AdminUpdatePasswordRequest;
import subport.admin.application.service.AdminAccountService;

@RestController
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

	private final AdminAccountService adminAccountService;

	@PutMapping("/password")
	public ResponseEntity<Void> updatePassword(
		@AuthenticationPrincipal AdminUserDetails userDetails,
		@Valid @RequestBody AdminUpdatePasswordRequest request
	) {
		adminAccountService.updatePassword(userDetails.getAdminId(), request);

		return ResponseEntity.noContent()
			.build();
	}

	@GetMapping("/profile")
	public ResponseEntity<AdminProfileResponse> getProfile(@AuthenticationPrincipal AdminUserDetails userDetails) {
		return ResponseEntity.ok(
			adminAccountService.getProfile(userDetails.getAdminId())
		);
	}
}
