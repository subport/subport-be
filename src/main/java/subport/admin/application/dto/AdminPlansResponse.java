package subport.admin.application.dto;

import java.util.List;

public record AdminPlansResponse(
	List<AdminPlanResponse> plans
) {
}
