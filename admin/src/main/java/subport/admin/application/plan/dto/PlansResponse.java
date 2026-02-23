package subport.admin.application.plan.dto;

import java.util.List;

public record PlansResponse(
	List<PlanResponse> plans
) {
}
