package subport.admin.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.dto.AdminProfileResponse;
import subport.admin.application.dto.AdminUpdatePasswordRequest;
import subport.admin.application.port.LoadAdminPort;
import subport.admin.application.port.PasswordEncoderPort;
import subport.admin.domain.Admin;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdminAccountService {

	private final LoadAdminPort loadAdminPort;
	private final PasswordEncoderPort passwordEncoderPort;

	@Transactional
	public void updatePassword(
		Long adminId,
		AdminUpdatePasswordRequest request
	) {
		Admin admin = loadAdminPort.loadAdmin(adminId);

		if (!passwordEncoderPort.matches(request.oldPassword(), admin.getPassword())) {
			throw new CustomException(ErrorCode.ADMIN_CURRENT_PASSWORD_MISMATCH);
		}

		String newPassword = request.newPassword();
		if (!newPassword.equals(request.confirmPassword())) {
			throw new CustomException(ErrorCode.ADMIN_NEW_PASSWORD_CONFIRM_MISMATCH);
		}

		String encoded = passwordEncoderPort.encode(newPassword);
		admin.updatePassword(encoded);
	}

	@Transactional(readOnly = true)
	public AdminProfileResponse getProfile(Long adminId) {
		Admin admin = loadAdminPort.loadAdmin(adminId);

		return new AdminProfileResponse(
			admin.getNickname(),
			admin.getEmail()
		);
	}
}
