package subport.admin.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import subport.admin.application.port.LoadAdminPort;
import subport.admin.domain.Admin;
import subport.application.exception.CustomException;
import subport.application.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class AdminPersistenceAdapter implements
	LoadAdminPort {

	private final SpringDataAdminRepository adminRepository;

	@Override
	public Admin loadAdmin(String email) {
		return adminRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
	}
}
