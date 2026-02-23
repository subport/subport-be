package subport.admin.application.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.account.dto.GetAdminProfileResponse;
import subport.admin.domain.Admin;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminAccountQueryService {

	private final LoadAdminPort loadAdminPort;

	public GetAdminProfileResponse getProfile(Long adminId) {
		Admin admin = loadAdminPort.loadAdmin(adminId);

		return new GetAdminProfileResponse(
			admin.getNickname(),
			admin.getEmail()
		);
	}
}
