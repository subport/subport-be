package subport.admin.adapter.out.persistence.account;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.account.LoadAdminPort;
import subport.admin.application.exception.AdminErrorCode;
import subport.admin.domain.Admin;

@Component
@RequiredArgsConstructor
public class AdminPersistenceAdapter implements
	LoadAdminPort {

	private final SpringDataAdminRepository adminRepository;

	@Override
	public Admin loadAdmin(Long adminId) {
		return adminRepository.findById(adminId)
			.orElseThrow(() -> new subport.common.exception.CustomException(AdminErrorCode.ADMIN_NOT_FOUND));
	}

	@Override
	public Admin loadAdmin(String email) {
		return adminRepository.findByEmail(email)
			.orElseThrow(() -> new subport.common.exception.CustomException(AdminErrorCode.ADMIN_NOT_FOUND));
	}
}
