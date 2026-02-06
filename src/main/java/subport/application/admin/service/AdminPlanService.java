package subport.application.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.application.admin.dto.AdminPlanResponse;
import subport.application.admin.dto.AdminPlansResponse;
import subport.application.admin.port.AdminPlanPort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminPlanService {

	private final AdminPlanPort planPort;

	public AdminPlansResponse getPlans(Long subscriptionId) {
		return new AdminPlansResponse(
			planPort.loadPlans(subscriptionId).stream()
				.map(AdminPlanResponse::from)
				.toList()
		);
	}
}
