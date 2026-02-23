package subport.admin.application.plan;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subport.admin.application.plan.dto.PlanResponse;
import subport.admin.application.plan.dto.PlansResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanQueryService {

	private final PlanPort planPort;

	public PlansResponse getPlans(Long subscriptionId) {
		return new PlansResponse(
			planPort.loadPlans(subscriptionId).stream()
				.map(PlanResponse::from)
				.toList()
		);
	}

}
