package subport.application.admin.dto;

import java.util.List;

public record AdminPlansResponse(
	List<AdminPlanResponse> plans
) {
}
