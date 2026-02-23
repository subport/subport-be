package subport.admin.application.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.account.dto.UpdateAdminPasswordRequest;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.domain.Admin;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminAccountCommandService {

	private final LoadAdminPort loadAdminPort;
	private final PasswordEncoderPort passwordEncoderPort;

	public void updatePassword(
		Long adminId,
		UpdateAdminPasswordRequest request
	) {
		Admin admin = loadAdminPort.loadAdmin(adminId);

		if (!passwordEncoderPort.matches(request.oldPassword(), admin.getPassword())) {
			throw new subport.common.exception.CustomException(AdminErrorCode.ADMIN_CURRENT_PASSWORD_MISMATCH);
		}

		String newPassword = request.newPassword();
		if (!newPassword.equals(request.confirmPassword())) {
			throw new subport.common.exception.CustomException(AdminErrorCode.ADMIN_NEW_PASSWORD_CONFIRM_MISMATCH);
		}

		String encoded = passwordEncoderPort.encode(newPassword);
		admin.updatePassword(encoded);
	}
}
